/**
 * Ultima modificacion: 7/03/2022
 * 
 * Clase que crea un Nodo
 * @file Node.java
 */
import java.util.ArrayList;


public abstract class Node {
    //protected float dataF;
    //protected String dataS;
    //protected boolean dataB;
    protected String dataTot;
    protected ArrayList<Node> lista;
    protected int tipo; //boolean = 0, float 1, string 2, lista 3

	/**
	 * Regresa la data
	 */
    public String getDataTot(){
        return dataTot;
    }

    public Node getNodeEvaluated(){
        if (tipo==3){
            //es una expresion
            //obtener el primer valor 
            String first = lista.get(0).getDataTot();

            //OPERACIONES ARIMTETICAS + - * /
            if (first.equalsIgnoreCase("+")){
                //suma
                int suma = 0;
                lista.remove(0);
                for (int i = 0;i<lista.size();i++){
                    int valor = Integer.valueOf(lista.get(i).getNodeEvaluated().getDataTot());
                    suma +=valor;
                }
                return new Valor(suma);
            } else if (first.equalsIgnoreCase("-")) {
                //resta
                lista.remove(0);
                int resta = Integer.valueOf(lista.get(0).getNodeEvaluated().getDataTot());
                for (int i = 1;i<lista.size();i++){
                    int valor = Integer.valueOf(lista.get(i).getNodeEvaluated().getDataTot());
                    resta -=valor;
                }
                return new Valor(resta);
            } else if (first.equalsIgnoreCase("*")) {
                //multiplicacion
                int multiplicacion = 1;
                lista.remove(0);
                for (int i = 0;i<lista.size();i++){
                    int valor = Integer.valueOf(lista.get(i).getNodeEvaluated().getDataTot());
                    multiplicacion *=valor;
                }
                return new Valor(multiplicacion);
            } else if (first.equalsIgnoreCase("/")) {
                //division
                lista.remove(0);
                int division = Integer.valueOf(lista.get(0).getNodeEvaluated().getDataTot());
                for (int i = 1;i<lista.size();i++){
                    int valor = Integer.valueOf(lista.get(i).getNodeEvaluated().getDataTot());
                    division /=valor;
                }
                return new Valor(division);
            
            // INSTRUCCION QUOTE
            } else if (first.equalsIgnoreCase("'")||first.equalsIgnoreCase("QUOTE")) {
                //regresa el valor de la expresion sin evaluar
                ArrayList<Node> nExp = new ArrayList<Node>();
                for (int i=1;i<lista.size();i++){
                    nExp.add(lista.get(i));
                }
                return new Expresion(nExp);

            // DEFINICION FUNCIONES
            } else if (first.equalsIgnoreCase("DEFUN")) {
                //agrega una funcion con su nombre a la lista de funciones





                return new Valor(false);

            // SETQ
            } else if (first.equalsIgnoreCase("SETQ")) {
                //agrega una variable
                AlmacenFunYVar alm = new AlmacenFunYVar();
                String nombre =lista.get(1).getDataTot();
                Node variable =  lista.get(2).getNodeEvaluated();
                alm.addVar(nombre,variable);
                return new Valor(true);
                
            // PREDICADOS ATOM LIST EQUAL < >
            } else if (first.equalsIgnoreCase("ATOM")) {
                //revisa si es un valor o una lista
                if (lista.get(1).tipo==3){
                    return new Valor(false);
                } else {
                    return new Valor (true);
                }
            } else if (first.equalsIgnoreCase("LIST")) {
                //evaluar lista
                ArrayList<Node> nuevo = new ArrayList<Node>();
                //quitar la palabra lista
                lista.remove(0);
                for (Node expresion:lista){
                    nuevo.add(expresion.getNodeEvaluated());
                }
                return new Expresion(nuevo);
            } else if (first.equalsIgnoreCase("EQUAL")) {
                //revisa si son iguales los 2 elementos
                String n1 = lista.get(1).getNodeEvaluated().getDataTot();
                String n2 =lista.get(2).getNodeEvaluated().getDataTot();
                if(n1.equals(n2)){
                    return new Valor(true);
                } else {
                    return new Valor(false);
                }
            } else if (first.equalsIgnoreCase("<")) {
                //revisa si el segundo es mayor
                String n1 = lista.get(1).getNodeEvaluated().getDataTot();
                String n2 =lista.get(2).getNodeEvaluated().getDataTot();
                int n1num=0;
                int n2num=0;
                boolean numero = false;
                //determinar si es numero
                try {
                    n1num=Integer.valueOf(lista.get(1).getNodeEvaluated().getDataTot());
                    n2num=Integer.valueOf(lista.get(2).getNodeEvaluated().getDataTot());
                } catch (Exception e){
                    //no pasa nada
                }
                if (numero){
                    //comparar numeros
                    if (n1num<n2num){
                        return new Valor(true);
                    } else {
                        return new Valor(false);
                    }
                } else {
                    //comparar strings
                    if (n1.compareTo(n2)<0){
                        return new Valor(true);
                    } else {
                        return new Valor(false);
                    }
                }

            } else if (first.equalsIgnoreCase(">")) {
                //evalua si el primero es mayor
                String n1 = lista.get(1).getNodeEvaluated().getDataTot();
                String n2 =lista.get(2).getNodeEvaluated().getDataTot();
                int n1num=0;
                int n2num=0;
                boolean numero = false;
                //determinar si es numero
                try {
                    n1num=Integer.valueOf(lista.get(1).getNodeEvaluated().getDataTot());
                    n2num=Integer.valueOf(lista.get(2).getNodeEvaluated().getDataTot());
                } catch (Exception e){
                    //no pasa nada
                }
                if (numero){
                    //comparar numeros
                    if (n1num>n2num){
                        return new Valor(true);
                    } else {
                        return new Valor(false);
                    }
                } else {
                    //comparar strings
                    if (n1.compareTo(n2)>0){
                        return new Valor(true);
                    } else {
                        return new Valor(false);
                    }
                }
            // CONDICIONALES COND
            } else if (first.equalsIgnoreCase("COND")) {
                //revisar cada condicional, y si una es verdadera, ejecutar
                //primero quitar la palabra condicional
                lista.remove(0);
                //iterar
                for(Node nodo:lista){
                    String valorB = nodo.getLista().get(0).getNodeEvaluated().getDataTot();
                    if (valorB.equals("T")){
                        return nodo.getLista().get(1).getNodeEvaluated();
                    }
                }
                //si no se cumple nada
                return new Valor(false);

            } else {
                //evaluar lista
                ArrayList<Node> nuevo = new ArrayList<Node>();
                //quitar la palabra lista
                lista.remove(0);
                for (Node expresion:lista){
                    nuevo.add(expresion.getNodeEvaluated());
                }
                return new Expresion(nuevo);
            }
        } else {
            AlmacenFunYVar alm = new AlmacenFunYVar();
            //tiene que ser un valor
            //revisar si es una variable ya definida
            if (alm.getVariables().containsKey(dataTot)){
                return alm.getVariables().get(dataTot);
            }
            //si no era variable solo se devuelve el mismo nodo
            return this;
        }
    }

    /**
	 * Establece la data
	 */
    public void setDataTot(String s){
        this.dataTot = s;
    }

	/**
	 * Regresa la lista
	 */
    public ArrayList<Node> getLista(){
        return lista;
    }

	/**
	 * Establece la lista
	 */
    public void setLista(ArrayList<Node> l){
        this.lista = l;
    }

	/**
	 * Inicia un nodo principal
	 */
    public Node(){
        lista = new ArrayList<Node>();
    }

	/**
	 * Regresa el tipo
	 */
    public abstract int type();

    
}
