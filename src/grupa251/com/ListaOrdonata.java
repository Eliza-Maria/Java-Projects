package grupa251.com;

import java.util.Arrays;

public class ListaOrdonata implements Lista {

    private int[] elems=new int[0];
    private int lungime=0;

    public ListaOrdonata() {}

    public ListaOrdonata(int[] elems) {
        this.elems = elems;
        lungime=elems.length;
        Arrays.sort(this.elems,0,lungime);
    }

    public void addElement(Integer integer){
        elems = Arrays.copyOf(elems, lungime + 1);
        lungime++;
        int i=0;
        while(elems[i]<integer)
            i++;
        for(int j=lungime-1;j>i;j--)
            elems[j]=elems[j-1];
        elems[i]=integer;
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
        int vector[]={6,2,8};
        ListaOrdonata lo=new ListaOrdonata(vector);

        /*for(Integer i=0;i<10;i++)
            l.addElement(i);*/

        lo.addElement(7);

        lo.afisare();

    }

}
