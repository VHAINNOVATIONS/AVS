object GoToEntry: TGoToEntry
  Left = 782
  Top = 575
  ActiveControl = UrlEdit
  BorderStyle = bsDialog
  Caption = 'Go To...'
  ClientHeight = 99
  ClientWidth = 326
  Color = clBtnFace
  Font.Charset = DEFAULT_CHARSET
  Font.Color = clWindowText
  Font.Height = -11
  Font.Name = 'MS Sans Serif'
  Font.Style = []
  OldCreateOrder = False
  Position = poScreenCenter
  PixelsPerInch = 96
  TextHeight = 13
  object MainPanel: TPanel
    Left = 0
    Top = 0
    Width = 326
    Height = 99
    Align = alClient
    BevelOuter = bvNone
    TabOrder = 0
    object UrlEdit: TEdit
      Tag = 2
      Left = 7
      Top = 26
      Width = 311
      Height = 21
      Ctl3D = True
      MaxLength = 255
      ParentCtl3D = False
      ParentShowHint = False
      ShowHint = True
      TabOrder = 0
      OnChange = UrlEditChange
    end
    object GoBtn: TBitBtn
      Left = 84
      Top = 67
      Width = 75
      Height = 25
      Caption = '&Go'
      Font.Charset = DEFAULT_CHARSET
      Font.Color = clWindowText
      Font.Height = -11
      Font.Name = 'MS Sans Serif'
      Font.Style = []
      ModalResult = 1
      ParentFont = False
      TabOrder = 1
      NumGlyphs = 2
      Spacing = 5
    end
    object CancelButton: TBitBtn
      Left = 167
      Top = 67
      Width = 75
      Height = 25
      Caption = '&Cancel'
      Font.Charset = DEFAULT_CHARSET
      Font.Color = clWindowText
      Font.Height = -11
      Font.Name = 'MS Sans Serif'
      Font.Style = []
      ModalResult = 2
      ParentFont = False
      TabOrder = 2
      NumGlyphs = 2
      Spacing = 5
    end
    object Panel1: TPanel
      Left = 7
      Top = 5
      Width = 310
      Height = 18
      BevelOuter = bvNone
      TabOrder = 3
      object FormatLabel: TLabel
        Left = 3
        Top = 0
        Width = 307
        Height = 18
        Align = alClient
        Caption = 'Enter URL:'
        Color = clWhite
        ParentColor = False
        Transparent = True
        ExplicitWidth = 53
        ExplicitHeight = 13
      end
      object TitleLabel: TLabel
        Left = 0
        Top = 0
        Width = 3
        Height = 18
        Align = alLeft
        Transparent = True
        ExplicitHeight = 13
      end
    end
  end
end
