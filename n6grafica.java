import java.util.Locale;
import java.util.Scanner;

public class n6grafica{
    public static void main(String[] args) {
        System.out.println("Bosquejo de grafica con texto");
        String texto =  ("Introduzca dos numeros reales separados por un espacio que \n"
        + "represnenten los extremos del intervalo\n "
        + "Seguido de una expresion que represente una funcion\n"
        + "Funciones validas Senx=Sx,Cosx=Cx,Tanx=Tx,Cscx=Wx,Secx=Qx,Ctgx=Rx,Expx=Ex,\n"
        + "Logx=Lx,Arcsenx=Ix,Arcos=Ux,Arctan=Ox,Arcscx=Px,Arsecx=Gx,Arctgx=Fx\n"
         +"Si se rquiere poner un signo negativo es necesario agregar el cero antes -x=(0-x)\n"
                + "Por ejemplo '-5 5 E(0-3*S(x))' es equivalente a graficar \n"
                + "exp(-Sin(x)) en el intervalo [-5,5]\n");
        System.out.println(texto);
        Scanner sc = new Scanner(System.in);
        sc.useLocale(Locale.US);
        double a = sc.nextDouble();
        double b = sc.nextDouble();
        String expresion = sc.nextLine();  
        Grafica g = new Grafica(a,b);
        Arbol f =new Arbol(expresion);
        System.out.print("f(x)=");
        f.inorder(f.obtenraiz());
        Arbol r = f.derivada();
        System.out.println();
        System.out.print("f'(x)=");
        r.inorder(r.obtenraiz());
        r.evaluar(1);
        g.agregarfuncion(f);  
        g.print();
    }

}

class Grafica {

    String [][] matriz;
    double [] x;
    public Grafica(){
        matriz= new String[25][75];
        for(int x=0;x<25;x++)
            for(int y=0;y<75;y++)
                matriz[x][y]= " ";
    }
    public Grafica(double a,double b){
        matriz= new String[25][75];
        for(int y=0;y<25;y++)
            for(int x=0;x<75;x++)
                matriz[y][x]= " ";
        
        x = new double[75];
        double h = (b-a)/75.0;
        for(int i=0;i<75;i++){
            x[i] = a+h*(i+1);       
        }
        
        for(int i=0;i<75;i++){
            
            for(int j=0;j<25;j++){
                if( (a+(i)*h)<0 && 0<=(a+(i+1)*h)){
                   if(matriz[j][i].isBlank())
                       matriz[j][i]="|";
                    
                }   
            }
         }
        
        
    }
    
    public void print(){
        for(int y=0;y<25;y++){
            for(int x=0;x<75;x++)
                System.out.print(matriz[y][x]);
        System.out.println();
        }
        
    }
    public void agregarfuncion(Arbol f){
       
        double []y=new double[75];
        
        for(int i=0;i<75;i++){
            y[i]=f.evaluar(x[i]);
        }
        
        Arbol df = f.derivada();
        
        double []dy = new double[75];
        for(int i=0;i<75;i++){
            dy[i]=df.evaluar(x[i]);
        }
                
        double ymax=max(y);
       
        double ymin=min(y);
        
        double h = (ymax-ymin)/25;
        
        
        
        for(int i=0;i<75;i++){
            
            for(int j=0;j<25;j++){
                if( (ymax-(j+1)*h)<y[i] && y[i]<=(ymax-j*h)){
                    
                    if(-0.15<dy[i] && dy[i]<0.15)
                        matriz[j][i]= "-";
                    if(-0.15>dy[i])
                        matriz[j][i]= "\\";
                    if(dy[i]>0.15)
                        matriz[j][i]= "/";
                    
                }   
            }//-5 +5 Sx
            
        }
        
        for(int i=0;i<75;i++){
            
            for(int j=0;j<25;j++){
                if( (ymax-(j+1)*h)<0 && 0<=(ymax-j*h)){
                   if(matriz[j][i].isBlank())
                       matriz[j][i]="-";
                    
                }   
            }
           
        }
            
        
    }
    
    public static double max(double[] t) {
    double maximum = t[0];   // start with the first value
    for (int i=1; i<t.length; i++) {
        if (t[i] > maximum) {
            maximum = t[i];   // new maximum
        }
    }
    return maximum;
    }
    public static double min(double[] t) {
    double minimum = t[0];   // start with the first value
    for (int i=1; i<t.length; i++) {
        if (t[i] < minimum) {
            minimum = t[i];   // new maximum
        }
    }
    return minimum;
    }//end method max

    
}


class Arbol 
{
private Nodo raiz;

public Arbol(Nodo raiz)
    {
    this.raiz=raiz;
    }

public Arbol()
    {
    raiz=null;
    }
public Nodo obtenraiz()
    {
    return raiz;
    }
public Arbol(String expresion)//Constructor que transforma una cadena de caracteres en un arbol
    {
    Pila operandos=new Pila();//Se crea una pila que contiene los operadores
    Pila operadores=new Pila();//Una pila que contiene 
    for(int j=0;j<expresion.length();j++)//Indice que recorre los caracteres de la la cadena 
        {
        if(expresion.charAt(j)==')')//Si el operador es cierre de parentesis se extraen los operandos que estan antes del parentesis derecho se crean los arboles con esos operandos y se guardan como lo nuevos operandos.
            {
            Nodo aux=operadores.Pop();
            while(!aux.obtensimbolo().equals("("))
                    {
                    Nodo der=operandos.Pop();
                    if(esfuncion(aux.obtensimbolo().charAt(0)))
                        {
                        operandos.Push(new Nodo(aux.obtensimbolo(),der));
                        }
                    else{
                        Nodo izq=operandos.Pop();
                        operandos.Push(new Nodo(izq,aux.obtensimbolo(),der));
                        }
                    aux=operadores.Pop();
                    }
            }
        if(esfuncion(expresion.charAt(j))){
            operadores.Push(new Nodo(expresion.charAt(j)));
          }
        
        if(expresion.charAt(j)=='(')//Si es un parentesis se pone hasta arriba de la pila
            {
                operadores.Push(new Nodo("("));
            }
        if(Character.isDigit(expresion.charAt(j)))//Si el caracter es un digito 
           {
            String digito="";//Creamos una cadena que almacena todos los digitos que estan junto a el
            int i=0;
            for(i=j;Character.isDigit(expresion.charAt(i)) ||expresion.charAt(i)=='.';i++)
                {
                digito+=expresion.charAt(i);
        if(i+1==expresion.length())break;//Si el digito termina de correr nuesta expresion salimos del buncle
                }
            operandos.Push(new Nodo(digito));//Colocamos el digito en la pila de operandos
            j+=digito.length()-1;//Le sumamos los digitos que quitamos al contador que esta recorriendo la cadena de texto
            }
        if(expresion.charAt(j)=='x')//Si es variable se pone en la pila de los operandos
            {
            operandos.Push(new Nodo("x"));
            }
        if(esoperadorbinario(expresion.charAt(j)))//Si es operador binario
            {
    String op="";
        op+=expresion.charAt(j);//Pasamos el operador a una cadena de texto
    while(!operadores.esvacia() && prioridad(op)<=prioridad(operadores.obtencima()))//Si la prioridad el operador es menor a la del operador de la cima se extrae el operador y se aplica los dos ultimos operandos de la pila de operandos. 
                    {
                    Nodo aux=operadores.Pop();
                    Nodo der=operandos.Pop();
                    if(esfuncion(aux.obtensimbolo().charAt(0)))
                        {
                        operandos.Push(new Nodo(aux.obtensimbolo(),der));
                        }
                    else{
                        Nodo izq=operandos.Pop();
                        operandos.Push(new Nodo(izq,aux.obtensimbolo(),der));
                        }
                    }
    operadores.Push(new Nodo(expresion.charAt(j)));
            }
    }
//Una vez que se paso toda la expresion a las pilas se van extrayendo los operadores ya sea binario se extraen dos operandos y se crea el arbol se vuelve el ultimo operando si es unuario solo se extrae un operando.
    while(!operadores.esvacia())
                    {
                    Nodo aux=operadores.Pop();
                    Nodo der=operandos.Pop();
                    if(esfuncion(aux.obtensimbolo().charAt(0)))
                        {
                        operandos.Push(new Nodo(aux.obtensimbolo(),der));
                        }
                    else{
                        Nodo izq=operandos.Pop();
                        operandos.Push(new Nodo(izq,aux.obtensimbolo(),der));
                        }
                    }
    raiz=operandos.Pop();//Por ultimo volvemos raiz el unico operando que quedo en la pila este ya tiene el arbol que se fue construtendo
    }
public void simplifica()//Funcion que simplifica el arbol cuando tenemos casos como 0+1 que es simplemente 1 no es necesario almacenar el 0+
    {
    if(raiz!=null)//Verifica queel nodo no sea nulo
        {
    //Crea un arbol con su rama izquierda y luego uno con su derecha para que de manera recursiva se simplificquen 
        Arbol ramaizq=new Arbol(raiz.obtenramaizquierda());
        ramaizq.simplifica();
        Arbol ramader=new Arbol(raiz.obtenramaderecha());
        ramader.simplifica();
        raiz.nuevaramaizquierda(ramaizq.obtenraiz());
        raiz.nuevaramaderecha(ramader.obtenraiz());
        if(raiz.obtensimbolo().equals("+"))// Si en la raiz tenemos un +
            {
    // Si alguna rama es cero enteonces se queda unicamente con lo que esta en la rama que no es cero seria la nueva raiz
            if(raiz.obtenramaizquierda().obtensimbolo().equals("0"))
                {
                raiz=raiz.obtenramaderecha();
                return;
                }
            if(raiz.obtenramaderecha().obtensimbolo().equals("0"))
                {
                raiz=raiz.obtenramaizquierda();
                return;
                }
            }
        if(raiz.obtensimbolo().equals("-"))// Si en la raiz tenemos un +
            {
    // Si alguna rama es cero enteonces se queda unicamente con lo que esta en la rama que no es cero seria la nueva raiz
            if(raiz.obtenramaizquierda()!=null)
        if(raiz.obtenramaizquierda().obtensimbolo().equals("0"))
                        {
                        raiz.nuevaramaizquierda(null);
                        return;
                        }
            if(raiz.obtenramaderecha().obtensimbolo().equals("0"))
                {
                raiz=raiz.obtenramaizquierda();
                return;
                }
            }
            if(raiz.obtensimbolo().equals("*"))//De manera analoga si hay * y un operando es uno se queda con el otro si es cero un factor regresa un arbol que tiene el 0 como raiz
            {
            if(raiz.obtenramaizquierda().obtensimbolo().equals("1"))
                {
                raiz=raiz.obtenramaderecha();
                return;
                }
            if(raiz.obtenramaderecha().obtensimbolo().equals("1"))
                {
                raiz=raiz.obtenramaizquierda();
                return;
                }
            if(raiz.obtenramaizquierda().obtensimbolo().equals("0") || raiz.obtenramaderecha().obtensimbolo().equals("0"))
                {   
                raiz=new Nodo("0");
                return;
                }
            }
        if(raiz.obtensimbolo().equals("^"))//Si el operador es ^ i el exponente es 1 regresa unicamente lo que esta antes del operando si el exponente es 0 regresa un arbol cuya raiz es 1.
            {
            if(raiz.obtenramaderecha().obtensimbolo().equals("1"))
                {
                raiz=raiz.obtenramaizquierda();
                return;
                }
            if(raiz.obtenramaderecha().obtensimbolo().equals("0"))
                {
                raiz=new Nodo("1");
                return;
                }
            }
        }
    }
public static void inorder(Nodo raiz)//Recorrido in order pone parentesis antes del recorrido y al final para que sea entedible a la hora de mostrar lo en la pantalla. Aunque en algunos casos sera inecesario.
    {
    if(raiz!=null)
    {
            if(raiz.obtensimbolo().equals("+"))
        {
               inorder(raiz.obtenramaizquierda());
                System.out.print(raiz.obtensimbolo());
                inorder(raiz.obtenramaderecha());
                }
            if(raiz.obtensimbolo().equals("-"))
        {
               inorder(raiz.obtenramaizquierda());
                System.out.print(raiz.obtensimbolo());
        if(raiz.obtenramaderecha().obtenramaderecha()!=null)
            {
            System.out.printf("(");
            inorder(raiz.obtenramaderecha());
                    System.out.printf(")");
                    }
        else
            inorder(raiz.obtenramaderecha());
        }
            if(raiz.obtensimbolo().equals("*") || raiz.obtensimbolo().equals("/"))
        {
        if(raiz.obtenramaizquierda().obtenramaderecha()!=null)
            {
            System.out.printf("(");
            inorder(raiz.obtenramaizquierda());
                    System.out.printf(")");
                    }
        else
                   inorder(raiz.obtenramaizquierda());
                System.out.print(raiz.obtensimbolo());
        if(raiz.obtenramaderecha().obtenramaderecha()!=null)
            {
            System.out.printf("(");
            inorder(raiz.obtenramaderecha());
                    System.out.printf(")");
                    }
        else
            inorder(raiz.obtenramaderecha());
        }
            if(raiz.obtensimbolo().equals("^"))
        {
        if(raiz.obtenramaizquierda().obtenramaderecha()!=null)
            {
            System.out.printf("(");
            inorder(raiz.obtenramaizquierda());
                    System.out.printf(")");
                    }
        else
                   inorder(raiz.obtenramaizquierda());
                System.out.print(raiz.obtensimbolo());
        inorder(raiz.obtenramaderecha());
        }
            if(esfuncion(raiz.obtensimbolo().charAt(0)))
        {
                System.out.print(raiz.obtensimbolo());
        if(raiz.obtenramaderecha().obtenramaderecha()!=null)
            {
            System.out.printf("(");
            inorder(raiz.obtenramaderecha());
                    System.out.printf(")");
                    }
        else
            inorder(raiz.obtenramaderecha());
        }
    if(raiz.obtenramaderecha()==null && raiz.obtenramaizquierda()==null)
                System.out.print(raiz.obtensimbolo());
    }
}
public Arbol derivada()//Metodo que crea un arbol con la derivada 
    {
//Se crea de manera recursiva y los casos base son la derivada de x que es 1 y la derivada de un digito quees una constante y su derivada es cero en estos casos regresa el arbol con 1 o 0 respectivamente.
//Los demas se construyen con las reglas de derivacion 
    Arbol derivada=new Arbol();//Arbol a retonar
    Nodo dos=new  Nodo("2");
    Nodo uno=new  Nodo("1");
    if(raiz.obtensimbolo().equals("x"))
        derivada.raiz=new Nodo("1");
    if(Character.isDigit(raiz.obtensimbolo().charAt(0)))
        derivada.raiz=new Nodo("0");
//Si en la raiz hay una suma o resta derivamos el nodo que esta a la izquieda y el que esta a la derecha y regresamos un arbol con la raiz del mismo operador y apuntando su rama izquieda a la derivada de su rama izquierda y la derecha apuntando a derivada de la rama derecha
//Es decir si tenemos (+)
//		      /	\
//		    (u)	(v)
//Regresamos          (+)
//		      /	\
//		    (u')(v')
    if(raiz.obtensimbolo().equals("-") || raiz.obtensimbolo().equals("+") )
        {
        Arbol f=new Arbol(raiz.obtenramaizquierda());
        f=f.derivada();
        Arbol g=new Arbol(raiz.obtenramaderecha());
        g=g.derivada();
        if(raiz.obtensimbolo().equals("+"))
            derivada.raiz=new Nodo(f.obtenraiz(),"+",g.obtenraiz());
        else
            derivada.raiz=new Nodo(f.obtenraiz(),"-",g.obtenraiz());
        }
//Para la multiplacion y division se hace algo similar respetando sus reglas de derivacion
    if(raiz.obtensimbolo().equals("*"))
        {
        Arbol f=new Arbol(raiz.obtenramaizquierda());
        Arbol df=f.derivada();
        Arbol g=new Arbol(raiz.obtenramaderecha());
        Arbol dg=g.derivada();
        Nodo producto1=new Nodo(f.obtenraiz(),"*",dg.obtenraiz());
        Nodo producto2=new Nodo(g.obtenraiz(),"*",df.obtenraiz());
        derivada.raiz=new Nodo(producto1,"+",producto2);
        }
    if(raiz.obtensimbolo().equals("/"))
        {
        Arbol f=new Arbol(raiz.obtenramaizquierda());
        Arbol df=f.derivada();
        Arbol g=new Arbol(raiz.obtenramaderecha());
        Arbol dg=g.derivada();
        Nodo producto2=new Nodo(f.obtenraiz(),"*",dg.obtenraiz());
        Nodo producto1=new Nodo(g.obtenraiz(),"*",df.obtenraiz());
        Nodo dividendo=new Nodo(producto1,"-",producto2);
        Nodo divisor=new Nodo(g.obtenraiz(),"^",dos);
        derivada.raiz=new Nodo(dividendo,"/",divisor);
        }
//Para el operador ^
//Primero se le aplica la regla de la cadena se toma su argumento es decir 
//Si tenemos el arbol (^)
//		     /   \
//		    (u)  (digito)
//		    Primero derivamos (u)
// Luego transformamos digito a entero y le quitamos uno
// Entonces creamos un arbol de la sigueinte manera
// 			 (*)
// 			/  \
//                    (^)  (u')
//		     /   \
//		    (*)  (digito-1)
    //                 /  \
//          (digito)  (u)
if(raiz.obtensimbolo().equals("^"))
        {
        Arbol f=new Arbol(raiz.obtenramaizquierda());//Se crea un arbol con su rama izquierda y se deriva dicho arbol seria (u')
        Arbol df=f.derivada();
        int exponente=Integer.parseInt(raiz.obtenramaderecha().obtensimbolo());//Se convierte en entero el exponente 
        Nodo exp=new Nodo(Integer.toString(exponente-1));//Se le resta uno para que sea el nuevo expoente
        Nodo g=new Nodo(new Nodo(Integer.toString(exponente)),"*",new Nodo(raiz.obtenramaizquierda(),"^",exp));
        derivada.raiz=new Nodo(g,"*",df.obtenraiz());
        }
//Para todas la funciones trigonometricas, sus inversas,exp,log,abs El procedimiento es totalmente analogo respetando sus respectivas derivadas y aplicando la regla de la cadena.
//Si tenemos   (sen)
//		  \
//		  (u)
//Creamos un arbol 	(*)
//		    	/ \
//		    (cos) (u')
//			\
//			(u)
if(raiz.obtensimbolo().equals("S"))
        {
        Arbol g= new Arbol(raiz.obtenramaderecha());//Creamos un arbol con el argumento del coseno 
        g=g.derivada();// Lo derivamos 
        Nodo f=new Nodo("C",raiz.obtenramaderecha());//cresmo un arbol que tiene como raiz al coseno y como rama derecha el argumento del seno.
        derivada.raiz=new Nodo(f,"*",g.obtenraiz());//Multiplicamos la derivada de la funcion por la derivada de su argumento
        }
    //Coseno C
    if(raiz.obtensimbolo().equals("C"))
        {
        Arbol g= new Arbol(raiz.obtenramaderecha());
        g=g.derivada();
        Nodo f=new Nodo("S",raiz.obtenramaderecha());
        Nodo _f=new Nodo("-",f);
        derivada.raiz=new Nodo(_f,"*",g.obtenraiz());
        }
    //Tangente T
    if(raiz.obtensimbolo().equals("T"))
        {
        Arbol g= new Arbol(raiz.obtenramaderecha());
        g=g.derivada();
        Nodo f=new Nodo("W",raiz.obtenramaderecha());
        f=new Nodo(f,"^",dos);
        derivada.raiz=new Nodo(f,"*",g.obtenraiz());
        }
    //Cosecante W
    if(raiz.obtensimbolo().equals("W"))
        {
        Arbol g= new Arbol(raiz.obtenramaderecha());
        g=g.derivada();
        Nodo csc=new Nodo("W",raiz.obtenramaderecha());
        Nodo ctg=new Nodo("R",raiz.obtenramaderecha());
        Nodo producto=new Nodo(csc,"*",ctg);
        producto=new Nodo("-",producto);
        derivada.raiz=new Nodo(producto,"*",g.obtenraiz());
        }
    //Cosecante Q
    if(raiz.obtensimbolo().equals("Q"))
        {
        Arbol g= new Arbol(raiz.obtenramaderecha());
        g=g.derivada();
        Nodo sec=new Nodo("Q",raiz.obtenramaderecha());
        Nodo tan=new Nodo("T",raiz.obtenramaderecha());
        Nodo producto=new Nodo(sec,"*",tan);
        derivada.raiz=new Nodo(producto,"*",g.obtenraiz());
        }
    //Cotangente R
    if(raiz.obtensimbolo().equals("R"))
        {
        Arbol g= new Arbol(raiz.obtenramaderecha());
        g=g.derivada();
        Nodo f=new Nodo("Q",raiz.obtenramaderecha());
        f=new Nodo(f,"^",dos);
        f=new Nodo("-",f);
        derivada.raiz=new Nodo(f,"*",g.obtenraiz());
        }
    if(raiz.obtensimbolo().equals("E"))
        {
        Arbol g= new Arbol(raiz.obtenramaderecha());
        g=g.derivada();
        derivada.raiz=new Nodo(raiz,"*",g.obtenraiz());
        }
    if(raiz.obtensimbolo().equals("L"))
        {
        Arbol g= new Arbol(raiz.obtenramaderecha());
        g=g.derivada();
        derivada.raiz=new Nodo(g.obtenraiz(),"/",raiz.obtenramaderecha());
        }
    //Arcoseno I
    if(raiz.obtensimbolo().equals("I"))
        {
        Arbol g=new Arbol(raiz.obtenramaderecha());
        g=g.derivada();
        derivada.raiz=new Nodo(g.obtenraiz(),"/",new Nodo(new Nodo(uno,"-",new Nodo(raiz.obtenramaderecha(),"^",dos)),"^", new Nodo("0.5")));
        }
    //Arcocoseno U
    if(raiz.obtensimbolo().equals("U"))
        {
        Arbol g=new Arbol(raiz.obtenramaderecha());
        g=g.derivada();
        derivada.raiz=new Nodo(new Nodo("-",g.obtenraiz()),"/",new Nodo(new Nodo(uno,"-",new Nodo(raiz.obtenramaderecha(),"^",dos)),"^", new Nodo("0.5")));
        }
    //Arcotangente O
    if(raiz.obtensimbolo().equals("O"))
        {
        Arbol g=new Arbol(raiz.obtenramaderecha());
        g=g.derivada();
        derivada.raiz=new Nodo(g.obtenraiz(),"/",new Nodo(uno,"+",new Nodo(raiz.obtenramaderecha(),"^",dos)));
        }
    //Arcocosecante P
    if(raiz.obtensimbolo().equals("P"))
        {
        Arbol g=new Arbol(raiz.obtenramaderecha());
        g=g.derivada();
        derivada.raiz=new Nodo("-",new Nodo(g.obtenraiz(),"/",new Nodo(raiz.obtenramaderecha(),"*",new Nodo(new Nodo(new Nodo(raiz.obtenramaderecha(),"^",dos),"-",uno),"^", new Nodo("0.5")))));
        }
    //Arcosecante G
    if(raiz.obtensimbolo().equals("G"))
        {
        Arbol g=new Arbol(raiz.obtenramaderecha());
        g=g.derivada();
        derivada.raiz=new Nodo(g.obtenraiz(),"/",new Nodo(raiz.obtenramaderecha(),"*",new Nodo(new Nodo(new Nodo(raiz.obtenramaderecha(),"^",dos),"-",uno),"^", new Nodo("0.5"))));
        }
    //Arcotagente F
    if(raiz.obtensimbolo().equals("F"))
        {
        Arbol g=new Arbol(raiz.obtenramaderecha());
        g=g.derivada();
        derivada.raiz=new Nodo("-",new Nodo(g.obtenraiz(),"/",new Nodo(uno,"+",new Nodo(raiz.obtenramaderecha(),"^",dos))));
        }
    return derivada;
    }
public double evaluar(double x){
    String valor = raiz.obtensimbolo();
    if(esnumero(valor)){
        return Double.parseDouble(valor);
    }
    /*if(valor.charAt(0)=='x'){
        return x;
    }*/
    if(esoperadorbinario(valor.charAt(0))){
        char operador = valor.charAt(0);
        Arbol izq = new Arbol(raiz.obtenramaizquierda());
        Arbol der = new Arbol(raiz.obtenramaderecha());
        if(operador == '+'){
            return izq.evaluar(x)+der.evaluar(x);
        }
        if(operador == '-'){
            if(izq.obtenraiz()==null)
                return - der.evaluar(x);
            else if(der.obtenraiz()==null)
                return -izq.evaluar(x);
            else
                return izq.evaluar(x)-der.evaluar(x);
        }
        if(operador == '*'){
            return izq.evaluar(x)*der.evaluar(x);
        }
        if(operador == '/'){
            return izq.evaluar(x)/der.evaluar(x);
        }
        if(operador == '^'){
            return Math.pow(izq.evaluar(x),der.evaluar(x));
        }
    }
    
    if(esfuncion(valor.charAt(0))){
        char operador = valor.charAt(0);
        Arbol der = new Arbol(raiz.obtenramaderecha());
        if(operador == 'S')
            return Math.sin(der.evaluar(x));
        if(operador == 'C')
            return Math.cos(der.evaluar(x));
        if(operador == 'T')
            return Math.tan(der.evaluar(x));
        if(operador == 'W')
            return 1.0/Math.sin(der.evaluar(x));
        if(operador == 'Q')
            return 1.0/Math.cos(der.evaluar(x));
        if(operador == 'R')
            return 1.0/Math.tan(der.evaluar(x));
        if(operador == 'E') 
            return Math.exp(der.evaluar(x));
        if(operador == 'L')
            return Math.log(der.evaluar(x));
        if(operador == 'I')
            return Math.asin(der.evaluar(x));
        if(operador == 'U')
            return Math.acos(der.evaluar(x));
        if(operador == 'O')
            return Math.atan(der.evaluar(x));
        if(operador == 'P')
            return Math.asin(1/der.evaluar(x));
        if(operador == 'G')
            return Math.acos(1/der.evaluar(x));
        if(operador == 'F')
            return Math.atan(1/der.evaluar(x));
        
    }
    return x;
}

static int prioridad(String funcion)//Priordad de operadoes
    {
int r=0;
 if(esfuncion(funcion.charAt(0)) || funcion.equals("~"))//Los unuarios tiene prioridad mayor a todos
     r=7;
 if(funcion.equals("^"))
     r=5;
 if(funcion.equals("*") || funcion.equals("/"))
     r=3;
 if(funcion.equals("+") || funcion.equals("-"))
     r=1;
 return r;
 }
static boolean esfuncion(char funcion)//Metodo que verifica si una cadena de texto es una funcion depende de su tamano para poder compararla 
    {
    return (
            funcion == 'C' || funcion == 'S' || funcion == 'T' || funcion == 'E' ||
            funcion == 'L' || funcion == 'W' || funcion == 'Q' || funcion == 'R' ||
            funcion == 'I' || funcion == 'U' || funcion == 'O' || funcion == 'P' ||
            funcion == 'G' || funcion == 'F'
       );
    }
static boolean esoperadorbinario(char c)
    {
    return (c=='+' || c=='-' || c=='*' || c=='/' || c=='^');
    }
public static boolean esnumero(String str) { 
try {  
Double.parseDouble(str);  
return true;
} catch(NumberFormatException e){  
return false;  
 }  
}   
}

class Pila {
    private Nodopila cima;
    
    public Pila(){
        this.cima=null;
        }
    
    public void Push(Nodo a){
        this.cima=new Nodopila(a,cima);
        }
    public Nodo Pop(){
        Nodo ret=cima.obtennodo();
        this.cima=cima.obtenantecesor();
        return ret;
        }
    public boolean esvacia(){
        return cima==null;
        }
    public String obtencima(){
        return cima.obtennodo().obtensimbolo();
        }
}


class Nodopila 
    {
    private Nodopila antecesor;
    private Nodo nodoarbol;
    public Nodopila(Nodo node,Nodopila antecesor)
        {
        this.antecesor=antecesor;
        this.nodoarbol=node;
        }
    public Nodo obtennodo()
        {
        return nodoarbol;
        }
    public Nodopila obtenantecesor()
        {
        return antecesor;
        }
    }

class Nodo 
    {
    private String simbolo;
    private Nodo ramaizquierda;
    private Nodo ramaderecha;
    
    public Nodo(String simbolo){
         this.simbolo=simbolo;
         this.ramaderecha=null;
         this.ramaizquierda=null;
        }
    public Nodo(char simbolo){
         String aux="";
         aux+=simbolo;
         this.simbolo=aux;
         this.ramaderecha=null;
         this.ramaizquierda=null;
        }
    public Nodo(Nodo ramaizquierda,String simbolo,Nodo ramaderecha){
        this.ramaizquierda=ramaizquierda;
        this.simbolo=simbolo;
        this.ramaderecha=ramaderecha;
        }
    public Nodo(Nodo ramaizquierda,char simbolo,Nodo ramaderecha){
         String aux="";
         aux+=simbolo;
        this.ramaizquierda=ramaizquierda;
        this.simbolo=aux;
        this.ramaderecha=ramaderecha;
        }
    public Nodo(String simbolo,Nodo ramaderecha){
        this.ramaizquierda=null;
        this.simbolo=simbolo;
        this.ramaderecha=ramaderecha;
        }
    public String obtensimbolo()
        {
        return simbolo;
        }
    public Nodo obtenramaizquierda()
        {
        return ramaizquierda;
        }
    public Nodo obtenramaderecha()
        {
        return ramaderecha;
        }
    public void nuevaramaizquierda(Nodo ramaizquierda)
        {
        this.ramaizquierda=ramaizquierda;
        }
    public void nuevaramaderecha(Nodo ramaderecha)
        {
        this.ramaderecha=ramaderecha;
        }
}

