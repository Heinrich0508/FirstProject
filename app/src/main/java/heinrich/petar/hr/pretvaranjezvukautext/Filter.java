package heinrich.petar.hr.pretvaranjezvukautext;

import java.util.ArrayList;

public class Filter extends MainActivity {

    String test = "This is Filter Class";
    String[] rootsOnly=null;
    // 1 pretvori string u array
    public String[] stringToArray (String voiceInput){
        String [] array = voiceInput.split(" ");
        return array;
    }
    // 2 nadi korijene i njihove pozicije u arrayu
    public String readListReturnAdjectives(String[]convertedStringToArray){
        String adjective = "-1";
        String productNameToCompare = null;

        for (int i=0; i<convertedStringToArray.length;i++){
            if (convertedStringToArray[i].equals("VINO")){
                return convertedStringToArray[i-1];
            }
        }

        return adjective;
    }


    //Proba
    public String compareArrayWithProductList (String[] array){
        String productName = null;
        String returnProduct = null;
        int position = 0;
            for(int i=0; i<array.length -1; i++) {
                String toCompare = array[i].toUpperCase();
                for (int y=0; y < listOFAllProducts.size(); y++) {
                    productName = listOFAllProducts.get(y).getFullNameOfProduct();
                    if (toCompare.contains(productName)) {
                        position = i;
                        returnProduct = productName;//dodat na listu
                    }
                }
            }


        return position + returnProduct;
    }
    // 3 izlistaj rijeci koje se nalaze poziciju prije korijena

    // 4 Usporedi izlistane sa koraka 3 sa pridjevima (nova lista), ako je pridjev vrati

    //Sastavi rezultat string sa koraka 4 i 2
    public String getTest() {
        return test;
    }
}
