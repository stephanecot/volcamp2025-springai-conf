public class badprog {

    public static void main(String[] args) {
        badprog bp = new badprog();
        bp.doStuff();
    }


    public void doStuff() {
        int a=10, b=5, c=0; String message="";
        for(int i=0;i<5;i++){
            c=a+b+i;
            if(c%2==0){
                message=message+"even:"+c+";";
            }else{
                message=message+"odd:"+c+";";
            }
        }
        System.out.println("msg="+message);
        printNumbers(3);
        printNumbers(7);
        calcSomething(123);
    }

    public void printNumbers(int limit){
        for(int k=0;k<=limit;k++){
            System.out.println("num="+k);
            if(k==2){
                System.out.println("TWO!!");
            } else if(k==3){
                System.out.println("THREE!!");
            } else if(k==4){
                System.out.println("FOUR!!");
            }
        }
    }

    public int calcSomething(int value){
        int res=0;
        for(int x=0;x<value;x++){
            res=res+x;
        }
        System.out.println("calc="+res);
        return res;
    }
}
