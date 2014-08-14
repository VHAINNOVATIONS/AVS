object WebClientView: TWebClientView
  Left = 530
  Top = 333
  ClientHeight = 230
  ClientWidth = 422
  Color = clBtnFace
  DefaultMonitor = dmDesktop
  Font.Charset = DEFAULT_CHARSET
  Font.Color = clWindowText
  Font.Height = -11
  Font.Name = 'MS Sans Serif'
  Font.Style = []
  Menu = MainMenu1
  OldCreateOrder = False
  PopupMode = pmAuto
  Position = poDesigned
  Scaled = False
  OnCreate = FormCreate
  OnDestroy = FormDestroy
  PixelsPerInch = 96
  TextHeight = 13
  object MainMenu1: TMainMenu
    Left = 112
    Top = 80
    object FileMenuItem: TMenuItem
      Caption = '&File'
      object GoToMenuItem: TMenuItem
        Caption = '&Go To...'
        OnClick = GoToMenuItemClick
      end
      object RefreshMenuItem: TMenuItem
        Caption = '&Reset'
        OnClick = RefreshMenuItemClick
      end
      object N1: TMenuItem
        Caption = '-'
      end
      object ExitMenuItem: TMenuItem
        Caption = 'E&xit'
        OnClick = ExitMenuItemClick
      end
    end
  end
end
