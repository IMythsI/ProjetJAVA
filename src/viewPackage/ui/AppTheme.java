package viewPackage.ui;

import java.awt.*;

public final class AppTheme {

    private AppTheme() {

    }

    //COLOR
    public static final Color BACKGROUND = new Color(245, 247, 250);
    public static final Color NAVBAR = new Color(24, 34, 52);
    public static final Color CARD = Color.WHITE;

    public static final Color PRIMARY = new Color(70, 120, 240);
    public static final Color PRIMARY_DARK = new Color(45, 95, 215);
    public static final Color PRIMARY_LIGHT = new Color(235, 241, 255);

    public static final Color SUCCESS = new Color(70, 180, 90);
    public static final Color WARNING = new Color(230, 150, 40);
    public static final Color DANGER = new Color(220, 70, 70);
    public static final Color INFO = new Color(90, 140, 220);

    public static final Color TEXT_PRIMARY = new Color(25, 25, 25);
    public static final Color TEXT_SECONDARY = new Color(110, 115, 125);
    public static final Color TEXT_LIGHT = new Color(150, 155, 165);

    public static final Color BORDER = new Color(215, 220, 230);
    public static final Color SEPARATOR = new Color(235, 238, 242);
    public static final Color HOVER = new Color(245, 248, 255);

    //FONTS
    public static final String FONT_FAMILY = "Arial";
    public static final String EMOJI_FONT_FAMILY = "Segoe UI Emoji";

    public static final Font TITLE_FONT =
            new Font(FONT_FAMILY, Font.BOLD, 34);

    public static final Font SUBTITLE_FONT =
            new Font(FONT_FAMILY, Font.PLAIN, 15);

    public static final Font SECTION_TITLE_FONT =
            new Font(FONT_FAMILY, Font.BOLD, 22);

    public static final Font CARD_TITLE_FONT =
            new Font(FONT_FAMILY, Font.BOLD, 20);

    public static final Font CARD_DESCRIPTION_FONT =
            new Font(FONT_FAMILY, Font.PLAIN, 12);

    public static final Font TEXT_FONT =
            new Font(FONT_FAMILY, Font.PLAIN, 14);

    public static final Font TEXT_BOLD_FONT =
            new Font(FONT_FAMILY, Font.BOLD, 14);

    public static final Font BUTTON_FONT =
            new Font(FONT_FAMILY, Font.BOLD, 14);

    public static final Font SMALL_BUTTON_FONT =
            new Font(FONT_FAMILY, Font.BOLD, 12);

    public static final Font NAVBAR_FONT =
            new Font(FONT_FAMILY, Font.BOLD, 22);

    public static final Font EMOJI_FONT =
            new Font(EMOJI_FONT_FAMILY, Font.PLAIN, 26);
    public static final Font SMALL_EMOJI_FONT =
            new Font(EMOJI_FONT_FAMILY, Font.PLAIN, 16);

    //LAYOUT
    public static final int WINDOW_MIN_WIDTH = 1000;
    public static final int WINDOW_MIN_HEIGHT = 650;

    public static final int PAGE_HORIZONTAL_PADDING = 30;
    public static final int PAGE_VERTICAL_PADDING = 35;

    public static final int HEADER_HEIGHT = 70;
    public static final int HEADER_HORIZONTAL_PADDING = 25;

    public static final int COMPONENT_GAP_SMALL = 8;
    public static final int COMPONENT_GAP_MEDIUM = 15;
    public static final int COMPONENT_GAP_LARGE = 25;

    //CARD
    public static final int CARD_PADDING_TOP = 20;
    public static final int CARD_PADDING_LEFT = 25;
    public static final int CARD_PADDING_BOTTOM = 20;
    public static final int CARD_PADDING_RIGHT = 25;

    public static final int CARD_MAX_WIDTH = 1050;
    public static final int FORM_CARD_MAX_WIDTH = 760;
    public static final int SEARCH_CARD_MAX_WIDTH = 820;
    public static final int TABLE_CARD_MAX_WIDTH = 1100;

    public static final Dimension ROLE_CARD_SIZE =
            new Dimension(230, 130);

    //FORM
    public static final int FIELD_WIDTH = 360;
    public static final int FIELD_HEIGHT = 35;
    public static final int TEXT_AREA_HEIGHT = 130;

    public static final int FORM_LABEL_WIDTH = 170;
    public static final int FORM_ROW_VERTICAL_GAP = 12;
    public static final int FORM_ROW_HORIZONTAL_GAP = 18;

    public static final Dimension FIELD_SIZE =
            new Dimension(FIELD_WIDTH, FIELD_HEIGHT);

    public static final Dimension TEXT_AREA_SIZE =
            new Dimension(FIELD_WIDTH, TEXT_AREA_HEIGHT);

    //BUTTON
    public static final int BUTTON_HEIGHT = 46;
    public static final int BUTTON_WIDTH = 190;

    public static final int SECONDARY_BUTTON_WIDTH = 160;
    public static final int SMALL_ICON_BUTTON_WIDTH = 50;
    public static final int SMALL_ICON_BUTTON_HEIGHT = 50;

    public static final Dimension PRIMARY_BUTTON_SIZE =
            new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT);

    public static final Dimension SECONDARY_BUTTON_SIZE =
            new Dimension(SECONDARY_BUTTON_WIDTH, BUTTON_HEIGHT);

    public static final Dimension BACK_BUTTON_SIZE =
            new Dimension(115, 38);

    public static final Dimension SMALL_ICON_BUTTON_SIZE =
            new Dimension(SMALL_ICON_BUTTON_WIDTH, SMALL_ICON_BUTTON_HEIGHT);

    public static final Dimension PRODUCT_BUTTON_SIZE =
            new Dimension(165, 90);

    public static final Dimension CART_LINE_SIZE =
            new Dimension(280, 44);

    //TABLE
    public static final int TABLE_ROW_HEIGHT = 72;
    public static final int JTABLE_ROW_HEIGHT = 34;
    public static final int TABLE_HEADER_HEIGHT = 45;

    public static final int TABLE_MIN_HEIGHT = 150;
    public static final int TABLE_BASE_HEIGHT = 90;

    public static final int TABLE_ROW_HORIZONTAL_PADDING = 25;
    public static final int TABLE_ROW_VERTICAL_PADDING = 10;

    //ORDER DETAIL
    public static final int ORDER_DETAIL_ROW_HEIGHT = 50;
    public static final int ORDER_DETAIL_CARD_MIN_WIDTH = 820;
    public static final int ORDER_DETAIL_CARD_HEIGHT = 420;
    public static final int ORDER_DETAIL_STATUS_COMBO_WIDTH = 170;
    public static final int ORDER_DETAIL_STATUS_COMBO_MIN_WIDTH = 140;
    public static final int ORDER_DETAIL_STATUS_COMBO_MAX_WIDTH = 220;

    public static final double ORDER_DETAIL_PRODUCT_WEIGHT = 0.24;
    public static final double ORDER_DETAIL_QUANTITY_WEIGHT = 0.10;
    public static final double ORDER_DETAIL_EMPLOYEE_WEIGHT = 0.18;
    public static final double ORDER_DETAIL_CURRENT_STATUS_WEIGHT = 0.16;
    public static final double ORDER_DETAIL_NEW_STATUS_WEIGHT = 0.22;
    public static final double ORDER_DETAIL_ACTION_WEIGHT = 0.10;

    //FLATLAF
    public static final String ROUND_BUTTON_STYLE =
                    "focusWidth:1;" +
                    "innerFocusWidth:0;" +
                    "borderWidth:1";

    public static final String ROUND_FIELD_STYLE =
                    "focusWidth:1;" +
                    "innerFocusWidth:0";

    public static final String ROUND_CARD_STYLE = "";

    //RADIUS
    public static final int BUTTON_ARC = 28;
    public static final int SMALL_BUTTON_ARC = 18;
    public static final int FIELD_ARC = 16;
    public static final int CARD_ARC = 20;
}