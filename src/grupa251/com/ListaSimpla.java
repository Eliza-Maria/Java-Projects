package grupa251.com;

import java.util.Arrays;

public class ListaSimpla implements Lista {

    private int[] elems=new int[0];
    private int lungime=0;

    public ListaSimpla() { }

    public ListaSimpla(int[] elems) {
        this.elems = elems;
        lungime=elems.length;
    }

    public void addElement(Integer integer){
                elems = Arrays.copyOf(elems, lungime + 1);
                elems[lungime]=integer;
                lungime++;
    }

    public boolean removeElement(Integer integer){
        for(int i=0;i<lungime;i++){
            if(integer==elems[i])
            {
                for(int j=i;j<lungime-1;j++)
                    elems[j]=elems[j+1];
                lungime--;
                elems = Arrays.copyOf(elems, lungime);
                return true;
            }
        }
        return false;
    }

    public int removeAll(Integer integer){
        int nr=0;
        for(int i=0;i<lungime;i++){
            if(integer==elems[i])
            {
                nr++;

                for(int j=i;j<lungime-1;j++)
                    elems[j]=elems[j+1];
                lungime--;
                elems = Arrays.copyOf(elems, lungime);

            }
        }
        return nr;
    }
    public Integer get(int index){
        if(index<=lungime)
            return elems[index];
        return 0;
    }
    public int lungime(){
        return lungime;
    }
    public void afisare(){
        for(int i : elems)
            System.out.println(i);
    }

    public static void main(String[] args) {

        int vector[]={0,1,2,3,4,5,6,7,8,9};
        ListaSimpla l=new ListaSimpla(vector);

        l.addElement(3);


        l.afisare();
        l.removeAll(3);
        l.afisare();

        System.out.println(l.get(0));
    }
}
