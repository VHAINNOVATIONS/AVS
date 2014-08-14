unit GoToForm;

interface

uses
  Windows, Messages, SysUtils, Classes, Graphics, Controls, Forms,
  Dialogs, StdCtrls, Buttons, ExtCtrls;

type
  TGoToEntry = class(TForm)
    MainPanel: TPanel;
    UrlEdit: TEdit;
    GoBtn: TBitBtn;
    CancelButton: TBitBtn;
    Panel1: TPanel;
    FormatLabel: TLabel;
    TitleLabel: TLabel;
    procedure UrlEditChange(Sender: TObject);
  private
    { Private declarations }
  public
    function GetUrl(var fUrl : string) : Boolean;
  end;

var
  GoToEntry: TGoToEntry;

implementation

{$R *.dfm}

function TGoToEntry.GetUrl(var fUrl : string) : Boolean;
var
  fTemp : string;
begin
  GoBtn.Enabled:=FALSE;
  Result:=ShowModal = mrOk;
  if Result then begin
    fUrl:=UrlEdit.Text;
  end;
end;

procedure TGoToEntry.UrlEditChange(Sender: TObject);
begin
  GoBtn.enabled:=Trim(UrlEdit.Text) <> '';
end;

end.
