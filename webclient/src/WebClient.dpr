program WebClient;

uses
  Forms,
  Classes,
  Dialogs,
  SysUtils,
  WebClientForm in 'WebClientForm.pas' {WebClientView},
  LL_StringUtils in '..\..\common\utils\LL_StringUtils.pas',
  LL_SysUtils in '..\..\common\utils\LL_SysUtils.pas',
  LL_DateUtils in '..\..\common\utils\LL_DateUtils.pas',
  ContextorUnit in '..\..\common\vista\ContextorUnit.pas',
  GoToForm in 'GoToForm.pas' {GoToEntry},
  UserMsgForm in '..\..\common\dialogs\UserMsgForm.pas' {UserMsg};

{$R *.res}

var
  i            : integer;
  fParams      : TStringList;
  fTitle       : string;
  fUrl         : String;
  fHeight      : integer;
  fWidth       : integer;
  fStationNo   : string;
  fUserDuz     : string;
  fPatientDfn  : string;
  fServer      : string;
  fPort        : integer;
  fMsg         : TStringList;
begin
  Application.Initialize;
  fParams:=TStringList.create;
  for i:=1 to ParamCount do
    fParams.add(ParamStr(i));
  fTitle:=fParams.Values['title'];
  fUrl:=fParams.Values['url'];
  fStationNo:=fParams.Values['stationNo'];
  fUserDuz:=fParams.Values['userDuz'];
  fPatientDfn:=fParams.Values['patientDfn'];
  fHeight:=StrToIntDef(fParams.Values['height'], 700);
  fWidth:=StrToIntDef(fParams.Values['width'], 985);
  fServer:=fParams.Values['s'];
  if (fServer = '') then
    fServer:='vista.loma-linda.med.va.gov';  
  fPort:=StrToIntDef(fParams.Values['p'], 19205);
  Application.MainFormOnTaskbar := True;
  Application.Title := 'Web Client';
  Application.CreateForm(TWebClientView, WebClientView);
  Application.CreateForm(TGoToEntry, GoToEntry);
  Application.CreateForm(TUserMsg, UserMsg);

  with WebClientView do begin
    SetInitialContext(fTitle, fStationNo, fUserDuz, fPatientDfn, fUrl, fServer, fPort);
    SetSize(fHeight, fWidth);
    SetPositionCenter;
    Navigate;
  end;
  Application.Run;
end.
