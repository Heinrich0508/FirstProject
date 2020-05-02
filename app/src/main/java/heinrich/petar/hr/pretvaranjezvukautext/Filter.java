package heinrich.petar.hr.pretvaranjezvukautext;

import android.util.Log;

import java.util.ArrayList;

public class Filter  {

    String test = "This is Filter Class";
    String[] rootsOnly=null;
    // 1 pretvori string u array
    public String[] stringToArray (String voiceInput){
        String [] array = voiceInput.split(" ");
        return array;
    }
    // 2 nadi korijene i njihove pozicije u arrayu

    //
    public String readListReturnAdjectives(String[]convertedStringToArray){
        String adjective = "-1";
        String productNameToCompare = null;
        String productFromArray=null;

       for (int i=0; i<convertedStringToArray.length; i++){
            productNameToCompare = convertedStringToArray[i];
            for (int y=0;y<MainActivity.listOFAllProducts.size();y++){
                if (productNameToCompare.contains(MainActivity.listOFAllProducts.get(y).getFullNameOfProduct()) || productNameToCompare.matches(MainActivity.listOFAllProducts.get(y).getRootProduct())){
                    productNameToCompare = convertedStringToArray[i];
                }
            }
        }

        for (int i=0; i<convertedStringToArray.length;i++){

            if (convertedStringToArray[i].contains(productNameToCompare) || convertedStringToArray[i].matches(productNameToCompare)){
                return convertedStringToArray[i-1];

            }
        }
        return adjective;
    }
    // 3 izlistaj rijeci koje se nalaze poziciju prije korijena

    // 4 Usporedi izlistane sa koraka 3 sa pridjevima (nova lista), ako je pridjev vrati

    //Sastavi rezultat string sa koraka 4 i 2
    public String getTest() {
        return test;
    }
}
