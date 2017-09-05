package tafqeet;

import java.util.Objects;

/**
 * Created by mohab 2 on 03/12/2016.
 */
public class tafqeetFactory {
    public String ArabicNumber ;
    public static String oldNumber;
    public tafqeetFactory(String number) {
       this.ArabicNumber = convertNumberToArabicWords(number);
    }

    public static String convertNumberToArabicWords(String number) throws NumberFormatException {
        // check if the input string is number or not

            Double.parseDouble(number);
        oldNumber = number;
        // check if its floating point number or not
        if (number.contains(".")) { // yes
            // the number
            String theNumber = number.substring(0, number.indexOf('.'));
            // the floating point number
            String theFloat = number.substring(number.indexOf('.') + 1);
            if(theFloat.length()==1){
                theFloat+="0";
            }
            // check how many digits in the number 1:x 2:xx 3:xxx 4:xxxx 5:xxxxx
            // 6:xxxxxx
            if(theFloat.length()>=1 && !Objects.equals(theFloat.substring(0, 2), "00")){

                if(Double.parseDouble(theFloat)<=10) {
                    switch (theNumber.length()) {
                        case 1:
                            return " فقط " + convertOneDigits(theNumber) + " جنيهاً و " + convertTwoDigits(theFloat) + " قروش لاغير ";
                        case 2:
                            return " فقط " + convertTwoDigits(theNumber) + " جنيهاً و " + convertTwoDigits(theFloat) + " قروش لاغير ";
                        case 3:
                            return " فقط " + convertThreeDigits(theNumber) + " جنيهاً و " + convertTwoDigits(theFloat) + " قروش لاغير ";
                        case 4:
                            return " فقط " + convertFourDigits(theNumber) + " جنيهاً و " + convertTwoDigits(theFloat) + " قروش لاغير ";
                        case 5:
                            return " فقط " + convertFiveDigits(theNumber) + " جنيهاً و " + convertTwoDigits(theFloat) + " قروش لاغير ";
                        case 6:
                            return " فقط " + convertSixDigits(theNumber) + " جنيهاً و " + convertTwoDigits(theFloat) + " قروش لاغير ";
                        case 7:
                            return " فقط " + convertSevenDigits(theNumber) + " جنيهاً و " + convertTwoDigits(theFloat)+ " قروش لاغير ";
                        case 8:
                            return " فقط " + convertEightDigits(theNumber) + " جنيهاً و " + convertTwoDigits(theFloat) + " قروش لاغير ";
                        default:
                            return "";
                    }
                }else{
                    switch (theNumber.length()) {
                        case 1:
                            return " فقط "+ convertOneDigits(theNumber) + " جنيهاً و " + convertTwoDigits(theFloat) +" قرشاً لاغير ";
                        case 2:
                            return " فقط "+convertTwoDigits(theNumber) + " جنيهاً و " + convertTwoDigits(theFloat) +" قرشاً لاغير ";
                        case 3:
                            return " فقط "+convertThreeDigits(theNumber) + " جنيهاً و " + convertTwoDigits(theFloat) +" قرشاً لاغير ";
                        case 4:
                            return " فقط "+convertFourDigits(theNumber) + " جنيهاً و " + convertTwoDigits(theFloat) +" قرشاً لاغير ";
                        case 5:
                            return " فقط "+convertFiveDigits(theNumber) + " جنيهاً و " + convertTwoDigits(theFloat) +" قرشاً لاغير ";
                        case 6:
                            return " فقط "+convertSixDigits(theNumber) + " جنيهاً و " + convertTwoDigits(theFloat) +" قرشاً لاغير ";
                        case 7:
                            return " فقط "+convertSevenDigits(theNumber) + " جنيهاً و " + convertTwoDigits(theFloat)+" قرشاً لاغير ";
                        case 8:
                            return " فقط "+convertEightDigits(theNumber) + " جنيهاً و " + convertTwoDigits(theFloat) +" قرشاً لاغير ";
                        default:
                            return "";
                    }
                }
            }else{
                switch (theNumber.length()) {
                    case 1:
                        return " فقط "+convertOneDigits(theNumber) + " جنيهاً لا غير " ;
                    case 2:
                        return " فقط "+convertTwoDigits(theNumber) + " جنيهاً لا غير " ;
                    case 3:
                        return " فقط "+convertThreeDigits(theNumber) + " جنيهاً لا غير " ;
                    case 4:
                        return " فقط "+convertFourDigits(theNumber) + " جنيهاً لا غير " ;
                    case 5:
                        return " فقط "+convertFiveDigits(theNumber) + " جنيهاً لا غير " ;
                    case 6:
                        return " فقط "+convertSixDigits(theNumber) + " جنيهاً لا غير " ;
                    case 7:
                        return " فقط "+convertSevenDigits(theNumber) + " جنيهاً لا غير " ;
                    case 8:
                        return " فقط "+convertEightDigits(theNumber) + " جنيهاً لا غير " ;
                    default:
                        return "";
                }
            }
        }

        else {

            switch (number.length()) {
                case 1:
                    if(convertOneDigits(number).length()>0) {
                        return " فقط "+convertOneDigits(number) + " جنيهاً لا غير";
                    }else{
                        return convertOneDigits(number);
                    }
                case 2:
                    return " فقط "+convertTwoDigits(number)+" جنيهاً لا غير";
                case 3:
                    return " فقط "+convertThreeDigits(number)+" جنيهاً لا غير";
                case 4:
                    return " فقط "+convertFourDigits(number)+" جنيهاً لا غير";
                case 5:
                    return " فقط "+convertFiveDigits(number)+" جنيهاً لا غير";
                case 6:
                    return " فقط "+convertSixDigits(number)+" جنيهاً لا غير";
                default:
                    return "";
            }

        }
    }

    // -------------------------------------------

    private static String convertOneDigits(String oneDigit) {
        if((oldNumber.indexOf(oneDigit)==0
                &&(oldNumber.length()-oldNumber.indexOf(oneDigit))>3
        )
                ||oldNumber.indexOf(oneDigit)==1)
        {
            return oneDigitTafqeetWithoutLastLetter(oneDigit);
        }
        else if(oldNumber.indexOf(oneDigit)==oldNumber.length()-1){
            switch (Integer.parseInt(oneDigit)) {
                case 1:
                    return "احد";
                case 2:
                    return "إثني";
                case 3:
                    return "ثلاثة";
                case 4:
                    return "أربعة";
                case 5:
                    return "خمسة";
                case 6:
                    return "ستة";
                case 7:
                    return "سبعة";
                case 8:
                    return "ثمانية";
                case 9:
                    return "تسعة";
                default:
                    return "";
            }
        }else{
            return oneDigitTafqeetWithoutLastLetter(oneDigit);
        }
    }

    private static String oneDigitTafqeetWithoutLastLetter(String oneDigit) {
        switch (Integer.parseInt(oneDigit)) {
            case 1:
                return "واحد";
            case 2:
                return "إثنان";
            case 3:
                return "ثلاث";
            case 4:
                return "أربع";
            case 5:
                return "خمس";
            case 6:
                return "ست";
            case 7:
                return "سبع";
            case 8:
                return "ثماني";
            case 9:
                return "تسع";
            default:
                return "";
        }
    }

    private static String convertTwoDigits(String twoDigits) {
        String returnAlpha = "00";
        // check if the first digit is 0 like 0x
        if(twoDigits.length()==1&&twoDigits.charAt(0) == '0'){
            return "";
        }else {
            if (twoDigits.charAt(0) == '0' && twoDigits.charAt(1) != '0') { // yes
                // convert two digits to one
                return convertOneDigits(String.valueOf(twoDigits.charAt(1)));
            } else { // no
                // check the first digit 1x 2x 3x 4x 5x 6x 7x 8x 9x
                try{
                    getIntVal(twoDigits.charAt(1));
                }catch (StringIndexOutOfBoundsException e1){
                    twoDigits+="0";
                }

                switch (getIntVal(twoDigits.charAt(0))) {
                    case 1: { // 1x
                        if (getIntVal(twoDigits.charAt(1)) == 0) {
                            return "عشرة";
                        }
                        if (getIntVal(twoDigits.charAt(1)) == 1) {
                            return "أحد عشر";
                        }
                        if (getIntVal(twoDigits.charAt(1)) == 2) {
                            return "إثنى عشر";
                        } else {
                            return convertOneDigits(String.valueOf(twoDigits.charAt(1))) + " " + "عشر";
                        }
                    }
                    case 2: // 2x x:not 0
                        returnAlpha = "عشرون";
                        break;
                    case 3: // 3x x:not 0
                        returnAlpha = "ثلاثون";
                        break;
                    case 4: // 4x x:not 0
                        returnAlpha = "أربعون";
                        break;
                    case 5: // 5x x:not 0
                        returnAlpha = "خمسون";
                        break;
                    case 6: // 6x x:not 0
                        returnAlpha = "ستون";
                        break;
                    case 7: // 7x x:not 0
                        returnAlpha = "سبعون";
                        break;
                    case 8: // 8x x:not 0
                        returnAlpha = "ثمانون";
                        break;
                    case 9: // 9x x:not 0
                        returnAlpha = "تسعون";
                        break;
                    default:
                        returnAlpha = "";
                        break;
                }
            }

            // 20 - 99
            // x0 x:not 0,1

            if (twoDigits.length()==1)
                twoDigits+="0";
            if (convertOneDigits(String.valueOf(twoDigits.charAt(1))).length() == 0) {
                return returnAlpha;
            } else { // xx x:not 0
                return convertOneDigits(String.valueOf(twoDigits.charAt(1))) + " و " + returnAlpha;
            }
        }
    }

    private static String convertThreeDigits(String threeDigits) {
        // check the first digit x00
        switch (getIntVal(threeDigits.charAt(0))) {

            case 1: { // 100 - 199
                if (getIntVal(threeDigits.charAt(1)) == 0) { // 10x
                    if (getIntVal(threeDigits.charAt(2)) == 0) { // 100
                        return "مائه";
                    } else { // 10x x: is not 0
                        return "مائه" + " و " + convertOneDigits(String.valueOf(threeDigits.charAt(2)));
                    }
                } else {// 1xx x: is not 0
                    return "مائه" + " و " + convertTwoDigits(threeDigits.substring(1, 3));
                }
            }
            case 2: { // 200 - 299
                if (getIntVal(threeDigits.charAt(1)) == 0) { // 20x
                    if (getIntVal(threeDigits.charAt(2)) == 0) { // 200
                        return "مائتين";
                    } else { // 20x x:not 0
                        return "مائتين" + " و " + convertOneDigits(String.valueOf(threeDigits.charAt(2)));
                    }
                } else { // 2xx x:not 0
                    return "مائتين" + " و " + convertTwoDigits(threeDigits.substring(1, 3));
                }
            }
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9: { // 300 - 999
                if (getIntVal(threeDigits.charAt(1)) == 0) { // x0x x:not 0
                    if (getIntVal(threeDigits.charAt(2)) == 0) { // x00 x:not 0
                        return convertOneDigits(String.valueOf(threeDigits.charAt(0)))+ "مائه";
                    } else { // x0x x:not 0
                        return convertOneDigits(String.valueOf(threeDigits.charAt(0))) + "مائه" + " و "
                                + convertOneDigits(String.valueOf(threeDigits.charAt(2)));
                    }
                } else { // xxx x:not 0
                    return convertOneDigits(String.valueOf(threeDigits.charAt(0))) + "مائه" + " و "
                            + convertTwoDigits(threeDigits.substring(1, 3));
                }
            }

            case 0: { // 000 - 099
                if (threeDigits.charAt(1) == '0') { // 00x
                    if (threeDigits.charAt(2) == '0') { // 000
                        return "";
                    } else { // 00x x:not 0
                        return convertOneDigits(String.valueOf(threeDigits.charAt(2)));
                    }
                } else { // 0xx x:not 0
                    return convertTwoDigits(threeDigits.substring(1, 3));
                }
            }
            default:
                return "";
        }
    }

    private static String convertFourDigits(String fourDigits) {
        // xxxx
        switch (getIntVal(fourDigits.charAt(0))) {

            case 1: { // 1000 - 1999
                if (getIntVal(fourDigits.charAt(1)) == 0) { // 10xx x:not 0
                    if (getIntVal(fourDigits.charAt(2)) == 0) { // 100x x:not 0
                        if (getIntVal(fourDigits.charAt(3)) == 0) { // 1000
                            return "ألف";
                        } else { // 100x x:not 0
                            return "ألف" + " و " + convertOneDigits(String.valueOf(fourDigits.charAt(3)));
                        }
                    } else { // 10xx x:not 0
                        return "ألف" + " و " + convertTwoDigits(fourDigits.substring(2, 4));
                    }
                } else { // 1xxx x:not 0
                    return "ألف" + " و " + convertThreeDigits(fourDigits.substring(1, 4));
                }
            }
            case 2: { // 2000 - 2999
                if (getIntVal(fourDigits.charAt(1)) == 0) { // 20xx
                    if (getIntVal(fourDigits.charAt(2)) == 0) { // 200x
                        if (getIntVal(fourDigits.charAt(3)) == 0) { // 2000
                            return "ألفان";
                        } else { // 200x x:not 0
                            return "ألفان" + " و " + convertOneDigits(String.valueOf(fourDigits.charAt(3)));
                        }
                    } else { // 20xx x:not 0
                        return "ألفان" + " و " + convertTwoDigits(fourDigits.substring(2, 4));
                    }
                } else { // 2xxx x:not 0
                    return "ألفان" + " و " + convertThreeDigits(fourDigits.substring(1, 4));
                }
            }
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9: { // 3000 - 9999
                if (getIntVal(fourDigits.charAt(1)) == 0) { // x0xx x:not 0
                    if (getIntVal(fourDigits.charAt(2)) == 0) { // x00x x:not 0
                        if (getIntVal(fourDigits.charAt(3)) == 0) { // x000 x:not 0
                            return convertOneDigits(String.valueOf(fourDigits.charAt(0))) + " آلاف";
                        } else { // x00x x:not 0
                            return convertOneDigits(String.valueOf(fourDigits.charAt(0))) + " آلاف" + " و "
                                    + convertOneDigits(String.valueOf(fourDigits.charAt(3)));
                        }
                    } else { // x0xx x:not 0

                        return convertOneDigits(String.valueOf(fourDigits.charAt(0))) + " آلاف" + " و "
                                + convertTwoDigits(fourDigits.substring(2, 4));
                    }
                } else { // xxxx x:not 0
                    return convertOneDigits(String.valueOf(fourDigits.charAt(0))) + " آلاف" + " و "
                            + convertThreeDigits(fourDigits.substring(1, 4));
                }
            }

            default:
                return "";
        }
    }

    private static String convertFiveDigits(String fiveDigits) {
        if (convertThreeDigits(fiveDigits.substring(2, 5)).length() == 0) { // xx000
            // x:not
            // 0
            return convertTwoDigits(fiveDigits.substring(0, 2)) + " ألف ";
        } else { // xxxxx x:not 0
            return convertTwoDigits(fiveDigits.substring(0, 2)) + " ألفا " + " و "
                    + convertThreeDigits(fiveDigits.substring(2, 5));
        }
    }

    private static String convertSixDigits(String sixDigits) {

        if (convertThreeDigits(sixDigits.substring(2, 5)).length() == 0) { // xxx000
            // x:not
            // 0
            return convertThreeDigits(sixDigits.substring(0, 3)) + " ألف ";
        } else { // xxxxxx x:not 0
            return convertThreeDigits(sixDigits.substring(0, 3)) + " ألفا " + " و "
                    + convertThreeDigits(sixDigits.substring(3, 6));
        }
    }
    private static String convertSevenDigits(String sixDigits) {

        if (convertThreeDigits(sixDigits.substring(2, 5)).length() == 0) { // xxx000
            // x:not
            // 0
            return convertThreeDigits(sixDigits.substring(0, 3)) + " ألف ";
        } else { // xxxxxx x:not 0
            return  convertOneDigits(sixDigits.substring(0,1)) + " مليون و "+
                    convertThreeDigits(sixDigits.substring(1, 4)) + " ألفا " + " و "
                    + convertThreeDigits(sixDigits.substring(4, 7));
        }
    }
    private static String convertEightDigits(String sixDigits) {

        if (convertThreeDigits(sixDigits.substring(2, 5)).length() == 0) { // xxx000
            // x:not
            // 0
            return convertThreeDigits(sixDigits.substring(0, 3)) + " ألف ";
        } else { // xxxxxx x:not 0
            return  convertTwoDigits(sixDigits.substring(0,2)) + " مليون و "+
                    convertThreeDigits(sixDigits.substring(2, 5)) + " ألفا " + " و "
                    + convertThreeDigits(sixDigits.substring(5, 8));
        }
    }

    private static int getIntVal(char c) {
        return Integer.parseInt(String.valueOf(c));
    }

    // ----------------------------------------------------------


}
