package heinrich.petar.hr.pretvaranjezvukautext;

import java.util.ArrayList;

public class Filter extends MainActivity {

    String test = "This is Filter Class";
    // 1 pretvori string u array
    public String[] stringToArray (String voiceInput){
        String [] array = voiceInput.split(" ");
        return array;
    }
    // 2 nadi korijene i njihove pozicije u arrayu
    public String compareArrayWithProductList (String[] array){
        for (Products products : listOFAllProducts){
            if (myWord.contains(products.getFullNameOfProduct()) || myWord.matches(products.getRootProduct())){
                //specialProductsInList.add(products);
                return myWord;
            }
        }
        return null;
    }
    // 3 izlistaj rijeci koje se nalaze poziciju prije korijena

    // 4 Usporedi izlistane sa koraka 3 sa pridjevima (nova lista), ako je pridjev vrati

    //Sastavi rezultat string sa koraka 4 i 2
    public String getTest() {
        return test;
    }
}
