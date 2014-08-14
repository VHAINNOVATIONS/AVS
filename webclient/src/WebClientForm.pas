unit WebClientForm;

interface

uses
  // Standard
  Windows, Messages, SysUtils, Variants, Classes, Graphics, Controls, Forms,
  Dialogs, ExtCtrls, StrUtils, StdCtrls, Menus,
  // Forms
  GoToForm, UserMsgForm,
  // ActiveX/Ole
  ActiveX, OleCtrls,
  // LL Utils
  LL_StringUtils, LL_SysUtils,
  // Embedded browser
  SHDocVw_EWB, EwbCore, EmbeddedWB,
  // HTTP
  IdURI,
  // RPC Broker
  Trpcb, RpcSLogin, CCOWRPCBroker,
  // Contextor (CCOW)
  ContextorUnit;

type
  TWebClientView = class(TForm)
    MainMenu1: TMainMenu;
    FileMenuItem: TMenuItem;
    ExitMenuItem: TMenuItem;
    RefreshMenuItem: TMenuItem;
    GoToMenuItem: TMenuItem;
    N1: TMenuItem;
    procedure FormDestroy(Sender: TObject);
    procedure EmbeddedWebBrowserNavigateComplete2(ASender: TObject;
      const pDisp: IDispatch; var URL: OleVariant);
    procedure FormCreate(Sender: TObject);
    procedure ExitMenuItemClick(Sender: TObject);
    procedure RefreshMenuItemClick(Sender: TObject);
    procedure GoToMenuItemClick(Sender: TObject);
  private
    fTitle                 : string;
    fBaseUrl               : string;
    fFullUrl               : string;
    fStationNo             : string;
    fUserDuz               : string;
    fVistaServer           : string;
    fVistaToken            : string;
    fServer                : string;
    fPort                  : integer;
    fPatientDfn            : string;
    fPatientName           : string;
    fPatientNatIdNum       : string;
    fContextor             : TContextor;
    fUserContext           : TUserContext;
    fPatientContext        : TPatientContext;
    fEmbeddedWebBrowser    : TEmbeddedWB;
    fCCOWRPCBroker         : TCCOWRPCBroker;
    function CreateContextor(fAppTitle : string; fPassCode : string) : TContextor;
    function CheckUserContext : TUserContext;
    function CheckPatientContext : TPatientContext;
    procedure ContextPending(const aContextItemCollection: IDispatch;
                             var fAllowChange : boolean;
                             var fReason : string);
    procedure ContextCanceled;
    procedure ContextCommitted;
    function GetUserDuz : string;
    function EncodeParam(fParam : string) : string;
  protected
  public
    procedure Init;
    procedure SetInitialContext(fTitle, fStationNo, fUserDuz, fPatientDfn,
                                fUrl,fServer : string;fPort : integer);
    procedure SetSize(fHeight, fWidth : integer);
    procedure Navigate; overload;
    procedure Navigate(fUrl : String); overload;
    procedure Print;
    procedure ResizeBy(fPercent : integer);
    procedure SetPosition(fTop, fLeft : integer);
    procedure SetPositionCenter;
  end;

const
  CPRS_TITLE = 'VistA CPRS in use by:';

var
  WebClientView    : TWebClientView;
  GSaved8087CW     : Word;
  NeedToUnitialize : Boolean; // True if the OLE subsystem could be initialized successfully.

implementation

{$R *.dfm}

procedure TWebClientView.SetInitialContext(fTitle, fStationNo, fUserDuz, fPatientDfn,
                                           fUrl, fServer : string;fPort : integer);
begin
  self.fTitle:=fTitle;
  self.caption:=fTitle;
  self.fServer:=fServer;
  self.fPort:=fPort;
  if (fStationNo <> '') then
    self.fStationNo:=fStationNo;
  if (fUserDuz <> '') then
    self.fUserDuz:=fUserDuz;
  if (fPatientDfn <> '') then
    self.fPatientDfn:=fPatientDfn;
  self.fBaseUrl:=fUrl;
  if Pos(fBaseUrl, '?') = 0 then
    fBaseUrl:=fBaseUrl + '?'
  else
    fBaseUrl:=fBaseUrl + '&';
  Init;
end;

procedure TWebClientView.Init;
begin
  if fContextor <> nil then begin
    fUserContext:=CheckUserContext;
    // get the user's DUZ
    if (fUserContext <> nil) then begin
      if (fUserContext.fVistaToken <> '') then begin
        fVistaToken:=fUserContext.fVistaToken;
      end;
      if (fUserContext.fVistaServer <> '') then begin
        fVistaServer:=fUserContext.fVistaServer;
      end;
      if (self.fStationNo = '') AND (fUserContext.fStationNo <> '') then begin
        fStationNo:=fUserContext.fStationNo;
      end;
    end;
    if (fUserDuz = '') then begin
      fUserDuz:=GetUserDuz;
    end;
    fPatientContext:=CheckPatientContext;
  end;
end;

procedure TWebClientView.Navigate;
var
  i     : integer;
  fList : TStringList;
begin
  if fFullUrl = '' then begin
    fFullUrl:=fBaseUrl + 'stationNo=' + fStationNo + '&' +
              'userDuz=' + fUserDuz + '&' +
              'patientDfn=' + fPatientDfn + '&' +
              'patientName=' + EncodeParam(fPatientName) + '&' +
              'patientNatIdNum=' + fPatientNatIdNum + '&' +
              'vistaServer=' + EncodeParam(fVistaServer) + '&' +
              'vistaToken=' + EncodeParam(fVistaToken);
  end;
  Navigate(fFullUrl);
end;

procedure TWebClientView.Navigate(fUrl : String);
begin
  try
    fEmbeddedWebBrowser.Go(fUrl);
  except
  end;
end;

procedure TWebClientView.Print;
begin
  fEmbeddedWebBrowser.Print;
end;

function TWebClientView.CreateContextor(fAppTitle : string; fPassCode : string) : TContextor;
var
  ClassID: TCLSID;
  strOLEObject: string;
begin
  Result:=nil;
  try
    strOLEObject := 'Sentillion.Contextor';
    if (CLSIDFromProgID(PWideChar(WideString(strOLEObject)), ClassID) = S_OK) then begin
      Result:=TContextor.Create;
      if Result <> nil then begin
        with Result do begin
          SetContextPendingCallBack(ContextPending);
          SetContextCanceledCallBack(ContextCanceled);
          SetContextCommittedCallBack(ContextCommitted);
          JoinContext(fAppTitle, fPassCode, TRUE, FALSE);
        end;
      end;
    end;
  except
    // ignore
  end;
end;

function TWebClientView.CheckUserContext : TUserContext;
begin
  Result:=nil;
  if fContextor <> nil then begin
    // Check CCOW context for a user
    try
      Result:=fContextor.GetUserContext;
      if (Result <> nil) AND (Result.fVistaServer <> '') then begin
        fVistaServer:=Result.fVistaServer;
        if (self.fStationNo = '') then begin
          fStationNo:=Result.fStationNo;
        end;
        fContextor.SetStationNumber(fStationNo);
      end else
        fContextor.SetStationNumber(fStationNo);
    except
      // ignore
    end;
  end;
end;

function TWebClientView.CheckPatientContext : TPatientContext;
begin
  Result:=nil;
  if fContextor <> nil then begin
    // Check CCOW context for an open patient
    try
      Result:=fContextor.GetPatientContext;
      if (Result.fDfn <> '') then begin
        fPatientDfn:=Result.fDfn;
      end;
      if (Result.fName <> '') then begin
        fPatientName:=Result.fName;
      end;
      if (Result.fNatIdNum <> '') then begin
        fPatientNatIdNum:=Result.fNatIdNum;
      end;
    except
      // ignore
    end;
  end;
end;

function TWebClientView.GetUserDuz : string;
begin
  Result:='';
  if fContextor <> nil then begin
    try
      fCCOWRPCBroker:=TCCOWRPCBroker.Create(application);
      with fCCOWRPCBroker do begin
        Server:=fServer;
        ListenerPort:=fPort;
        KernelLogIn:=TRUE;
        Login.Mode:=lmAppHandle;
        Contextor:=fContextor.ContextorControl;
        if (fUserContext <> nil) then begin
          Login.NTToken:=fUserContext.fVistaToken;
          Login.LogInHandle:=fUserContext.fVistaToken;
        end;
        Connected:=TRUE;
        Result:=User.DUZ;
        Connected:=FALSE;
      end;
    except
    end;
  end;
end;

procedure TWebClientView.GoToMenuItemClick(Sender: TObject);
begin
  if GoToEntry.GetUrl(fFullUrl) then begin
    Navigate(fFullUrl);
  end;
end;

procedure TWebClientView.ContextPending(const aContextItemCollection: IDispatch;
                                var fAllowChange : boolean;
                                var fReason : string);
begin
  fAllowChange:=TRUE;
end;

procedure TWebClientView.ContextCanceled;
begin
  //
end;

procedure TWebClientView.ContextCommitted;
begin
  if fContextor.CurrentState = ContextorUnit.CCOW_ENABLED then begin
    fUserContext:=CheckUserContext;
    if (fUserContext <> nil) AND (fUserContext.fVistaToken <> fVistaToken) then begin
      fUserDuz:=GetUserDuz;
      fVistaToken:=fUserContext.fVistaToken;
      if (self.fStationNo = '') then begin
        fStationNo:=fUserContext.fStationNo;
      end;
      fVistaServer:=fUserContext.fVistaServer;
      fContextor.UpdateUserContext(fUserContext);
    end;
    fPatientContext:=CheckPatientContext;
    fContextor.UpdatePatientContext(fPatientContext);
    fFullUrl:='';
    Navigate;
  end;
end;

procedure TWebClientView.SetSize(fHeight, fWidth : integer);
begin
  self.ClientHeight:=fHeight;
  self.ClientWidth:=fWidth;
end;

procedure TWebClientView.RefreshMenuItemClick(Sender: TObject);
begin
  Navigate;
end;

procedure TWebClientView.ResizeBy(fPercent : integer);
var
  fResizeBy : double;
begin
  fResizeBy:=fPercent * 0.01;
  self.ClientHeight:=self.ClientHeight + Round(self.ClientHeight * fResizeBy);
  self.ClientWidth:=self.ClientWidth + Round(self.ClientWidth * fResizeBy);
end;

procedure TWebClientView.SetPosition(fTop, fLeft : integer);
begin
  self.Top:=fTop;
  self.Left:=fLeft;
end;

procedure TWebClientView.SetPositionCenter;
var
  fMonitor        : TMonitor;
  fLeft, fTop,
  fWidth, fHeight : integer;
  fCprsHwnd       : HWND;
begin
  fCprsHwnd:=LL_SysUtils.FindWindowEx(CPRS_TITLE);
  if (fCprsHwnd <> 0) then begin
    fMonitor:=Screen.MonitorFromWindow(fCprsHwnd, mdNearest);
    if (fMonitor <> nil) then begin
      fLeft:=fMonitor.Left;
      fTop:=fMonitor.Top;
      fHeight:=fMonitor.Height;
      fWidth:=fMonitor.Width;
    end;
  end else begin
    fLeft:=Screen.DesktopLeft;
    fTop:=Screen.DesktopTop;
    fHeight:=Screen.DesktopHeight;
    fWidth:=Screen.DesktopWidth;
  end;
  fLeft:=fLeft + ((fWidth div 2) - (self.Width div 2));
  fTop:=fTop + ((fHeight div 2) - (self.Height div 2));
  SetPosition(fTop, fLeft);
end;

procedure TWebClientView.EmbeddedWebBrowserNavigateComplete2(ASender: TObject;
      const pDisp: IDispatch; var URL: OleVariant);
begin
  fEmbeddedWebBrowser.UserInterfaceOptions:=[DontUse3DBorders];
end;

procedure TWebClientView.ExitMenuItemClick(Sender: TObject);
begin
  Application.Terminate;
end;

procedure TWebClientView.FormCreate(Sender: TObject);
begin
  fEmbeddedWebBrowser:=TEmbeddedWB.Create(nil);
  with fEmbeddedWebBrowser do begin
    Align:=alClient;
    Parent:=self;
    UserInterfaceOptions:=[DontUse3DBorders, DontUseScrollBars];
    OnNavigateComplete2:=EmbeddedWebBrowserNavigateComplete2;
  end;
  fContextor:=CreateContextor('Web Client', '');
end;

procedure TWebClientView.FormDestroy(Sender: TObject);
begin
  if Assigned(fEmbeddedWebBrowser) then begin
    fEmbeddedWebBrowser.Navigate('about:blank');
    Application.ProcessMessages;
    CoFreeUnusedLibraries();
    FreeAndNil(fEmbeddedWebBrowser);
  end;
end;

function TWebClientView.EncodeParam(fParam : string) : string;
begin
  fParam:=LL_StringUtils.ReplaceText(fParam, '&', '&#38;');
  Result:=TIdURI.ParamsEncode(fParam);
end;

initialization
  GSaved8087CW := Default8087CW;
  Set8087CW($133F);
  // Initialize OLE subsystem for drag'n drop and clipboard operations.
  NeedToUnitialize := Succeeded(OleInitialize(nil));

finalization
  Set8087CW(GSaved8087CW);
  if NeedToUnitialize then
  try
    OleUninitialize;
  except
  end;

end.
