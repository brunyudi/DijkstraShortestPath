/**
 * Aluno: Bruno Yudi Mino Okada
 *
 * Sua  tarefa  será  construir  um  grafo,  com  20  vértices  cujos  valores  serão  aleatoriamente
 * selecinados em um conjunto de inteiros contendo números inteiros entre 1 e 100. Cada vértice terá um
 * número aleatório de arestas menor ou igual a três. Cada aresta terá um peso definido por um número
 * inteiro positivo aleatoriament definido entre 1 e 20. Você deverá criar este grafo, armazenando estes
 * vértices  e  arestas  em  uma  tabela  de  adjacências  ou  em  uma  lista  de  adjacências,  nos  dois  caso
 * armazenando o peso definido para cada uma delas.
 * Seu  objetivo  será,  usando  o  algoritmo  de  menor  distância  de  Dijkstra,  imprimir  a  lista  de
 * menores  distâncias,  destacando  a  ordem  de  visitação  dos  vértices  entre  um  vertice  inicial  da  sua
 * escolha e todos os outros 19 vértices deste grafo.
 * Para que este programa possa ser verificado, você deverá imprir, além da lista de caminhos e
 * do  valor  da  menor  distância  uma  lista  de  pesos  de  cada  aresta  de  cada  um  dos  vértices  segundo  o
 * modelo:  [1,2,  19],  neste  conjunto,  o  primeiro  número  indica  o  vertice  1,  o  segundo  o  vertice  2  e  o
 * terceiro o peso da aresta entre os vértices 1 e 2.
 */


import java.util.*;

class Aresta implements Comparable<Aresta> {

    Vertice inicio;
    Vertice destino;
    int peso;

    // Construtor da Aresta
    Aresta(Vertice inic, Vertice dest, int p) {
        inicio = inic;
        destino = dest;
        peso = p;
    }

    public String toString() {
        return String.format("(%s -> %s, %d)", inicio.valor, destino.valor, peso);
    }


    public int compareTo(Aresta outraAresta) {

        if (this.peso > outraAresta.peso) {
            return 1;
        }
        else return -1;
    }
}

class Vertice {
    int valor;
    private boolean visitado;
    LinkedList<Aresta> arestas;
    int contador;  // Apenas para garantir que tenha no max 3 arestas

    // Construtor do Vertice
    Vertice(int valor) {
        this.valor = valor;
        visitado = false;
        arestas = new LinkedList<>();
        contador = 0;
    }

    // Verificar se o vertice ja foi visitado
    boolean isVisitado() {
        return visitado;
    }

    // Visitar o vertice
    void visitar() {
        visitado = true;
    }

    // Desvisitar o vertice
    void desvisitar() {
        visitado = false;
    }
}

class Grafo {
    private Set<Vertice> vertices;
    private boolean direcionado;

    // Construtor do Grafo
    Grafo(boolean direcionado) {
        this.direcionado = direcionado;
        vertices = new HashSet<>();
    }

    // Adicionar varios vertices de uma vez so
    public void adicionarVertice(Vertice... n) {
        vertices.addAll(Arrays.asList(n));
    }

    // Adicionar arestas no grafo
    public void adicionarAresta(Vertice inicio, Vertice destino, int peso) {
        vertices.add(inicio);
        vertices.add(destino);

        verificarAresta(inicio, destino, peso);

        if (!direcionado && inicio != destino) {
            verificarAresta(destino, inicio, peso);
        }
    }

    // Verificar se a aresta ja existe
    // Caso exista, atualiza o peso da aresta
    // Caso nao exista, cria a aresta
    private void verificarAresta(Vertice a, Vertice b, int peso) {
        for (Aresta aresta : a.arestas) {
            if (aresta.inicio == a && aresta.destino == b) {
                aresta.peso = peso;
                return;
            }
        }
        a.arestas.add(new Aresta(a, b, peso));
    }

    // Imprime as arestas dos vertices, incluindo os pesos
    public void imprimirAresta() {
        for (Vertice vertice : vertices) {
            LinkedList<Aresta> arestas = vertice.arestas;

            if (arestas.isEmpty()) {
                System.out.println("Vertice " + vertice.valor + " nao tem arestas");
                continue;
            }
            System.out.print("Vertice " + vertice.valor + " tem arestas para: ");

            for (Aresta aresta : arestas) {
                System.out.print(aresta.destino.valor + "(" + aresta.peso + ") ");
            }
            System.out.println();
        }
    }

    // Verifica se os vertices inseridos possuem arestas entre si
    public boolean temArestasCom(Vertice inicio, Vertice destino) {
        LinkedList<Aresta> arestas = inicio.arestas;
        for (Aresta aresta : arestas) {
            if (aresta.destino == destino) {
                return true;
            }
        }
        return false;
    }

    // Remove a condicao de visitado do Vertice
    public void resetarVerticesVisitados() {
        for (Vertice node : vertices) {
            node.desvisitar();
        }
    }

    // Funcao do Dijkstra Shortest Path entre dois vertices
    public void DijkstraShortestPath(Vertice inicio, Vertice destino) {
        HashMap<Vertice, Vertice> alterouEm = new HashMap<>();
        alterouEm.put(inicio, null);

        HashMap<Vertice, Integer> mapaCaminhoMaisCurto = new HashMap<>();

        // Para cada vertice do Set, caso o vertice inicial seja igual ao vertice
        // o peso entre eles e definido como 0
        for (Vertice vertice : vertices) {
            if (vertice == inicio)
                mapaCaminhoMaisCurto.put(inicio, 0);
                // Caso contrario, o peso e definido como MAX_VALUE para simular valor Infinito
            else mapaCaminhoMaisCurto.put(vertice, Integer.MAX_VALUE);
        }

        for (Aresta aresta : inicio.arestas) {
            mapaCaminhoMaisCurto.put(aresta.destino, aresta.peso);
            alterouEm.put(aresta.destino, inicio);
        }

        inicio.visitar();

        // Loop para realizar as acoes enquanto ainda tiver um Vertice nao visitado que
        // possa ser alcancado por algum outro vertice
        while (true) {
            Vertice verticeAtual = verticeMaisProximoNaoVisitado(mapaCaminhoMaisCurto);
            // Se o Vertice destino ainda nao foi alcancado e nao ha mais vertices nao
            // visitados que podem ser alcancados
            if (verticeAtual == null) {
                System.out.println("Nao ha arestas entre " + inicio.valor + " e " + destino.valor);
                System.out.println();
                return;
            }

            // Se o vertice atual foi o vertice destino, imprime o caminho e o peso total
            if (verticeAtual == destino) {
                System.out.println("O caminho mais curto entre "
                        + inicio.valor + " e " + destino.valor + " e:");

                Vertice filho = destino;

                String caminho = String.valueOf(destino.valor);
                while (true) {
                    Vertice pai = alterouEm.get(filho);
                    if (pai == null) {
                        break;
                    }

                    // Vai atualizando o caminho realizado
                    caminho = pai.valor + " " + caminho;
                    filho = pai;
                }
                System.out.println(caminho);
                System.out.println("O caminho tem peso: " + mapaCaminhoMaisCurto.get(destino));
                System.out.println();
                return;
            }
            verticeAtual.visitar();

            // Vai por todos os Vertices nao visitados com os quais o vertice atual possui uma aresta
            // e verifica se o valor do caminho e inferior ao que possuia anteriormente
            for (Aresta aresta : verticeAtual.arestas) {
                if (aresta.destino.isVisitado())
                    continue;

                if (mapaCaminhoMaisCurto.get(verticeAtual)
                        + aresta.peso
                        < mapaCaminhoMaisCurto.get(aresta.destino)) {
                    mapaCaminhoMaisCurto.put(aresta.destino,
                            mapaCaminhoMaisCurto.get(verticeAtual) + aresta.peso);
                    alterouEm.put(aresta.destino, verticeAtual);
                }
            }
        }
    }

    // Funcao para verificar qual e o Vertice mais proximo que ainda nao foi visitado
    private Vertice verticeMaisProximoNaoVisitado(HashMap<Vertice, Integer> mapCaminhoMaisCurto) {

        int caminhoMaisCurto = Integer.MAX_VALUE;
        Vertice verticeMaisProximo = null;
        for (Vertice vertice : vertices) {
            if (vertice.isVisitado())
                continue;

            int distanciaAtual = mapCaminhoMaisCurto.get(vertice);
            if (distanciaAtual == Integer.MAX_VALUE)
                continue;

            if (distanciaAtual < caminhoMaisCurto) {
                caminhoMaisCurto = distanciaAtual;
                verticeMaisProximo = vertice;
            }
        }
        return verticeMaisProximo;
    }
}

public class Main {
    public static void main(String[] args) {
        Grafo grafo = new Grafo(true);
        // Lista para armazenar os vertices
        List<Vertice> listaVertices = new ArrayList<>();
        // Lista para armazenar as arestas
        List<Aresta> listaArestas = new ArrayList<>();
        int maxVert = 100;
        int minVert = 1;
        int valorMin = 1;
        int valorMax = 20;

        // Gerador aleatorio de 20 numeros entre 1 e 100 para os vertices
        // e insere-os na lista de vertices
        for(int i = 0; i < 20; i++){
            int vert = (int)Math.floor(Math.random()*(maxVert-minVert+1)+minVert);
            Vertice vertice = new Vertice(vert);
            listaVertices.add(vertice);
        }


        // Gera numeros aleatorios entre 1 e 20 para os pesos das arestas
        // e pega aleatoriamente 2 vertices entre os 20 existentes para formar arestas
        // caso sejam escolhidos o mesmo vertice, o peso da aresta sera 0
        // caso um dos vertices ja tenha 3 arestas, nao e criado uma nova
        for(int j = 0; j < 250; j++){
            int peso = (int)Math.floor(Math.random()*(valorMax-valorMin+1)+valorMin);
            int vert1 = ((int)Math.floor(Math.random()*(valorMax-valorMin+1)+valorMin)-1);
            int vert2 = ((int)Math.floor(Math.random()*(valorMax-valorMin+1)+valorMin)-1);

            if (listaVertices.get(vert1).contador >= 3 || listaVertices.get(vert2).contador >= 3){
                //System.out.println("Um dos vertices inseridos ja possui 3 arestas!");
            } else if(vert1 == vert2){
                grafo.adicionarAresta(listaVertices.get(vert1), listaVertices.get(vert2), 0);
                Aresta aresta = new Aresta(listaVertices.get(vert1), listaVertices.get(vert2), 0);
                listaArestas.add(aresta);
                listaVertices.get(vert1).contador++;
            } else{
                grafo.adicionarAresta(listaVertices.get(vert1), listaVertices.get(vert2), peso);
                Aresta aresta = new Aresta(listaVertices.get(vert1), listaVertices.get(vert2), peso);
                listaArestas.add(aresta);
                listaVertices.get(vert1).contador++;
                listaVertices.get(vert2).contador++;
            }
        }

        // Imprime as arestas no formato pedido no enunciado
        System.out.println("Arestas: ");
        for(int x = 0; x < listaArestas.size(); x++){
            System.out.println("[" + listaArestas.get(x).inicio.valor+ ", " + listaArestas.get(x).destino.valor + ", "+ listaArestas.get(x).peso + "]");
        }

        // Caso fique dificil de ver as arestas, a forma seguinte (em comentario) e mais organizada
        // grafo.imprimirAresta();

        System.out.println();

        // Imprime o melhor caminho entre 2 vertices (caso tenha)
        System.out.println("Dijkstra Shortest Path: ");

        grafo.DijkstraShortestPath(listaVertices.get(0), listaVertices.get(19));

        grafo.DijkstraShortestPath((listaArestas.get(0).inicio), listaArestas.get(listaArestas.size()-1).destino);

        grafo.DijkstraShortestPath(listaVertices.get(5), listaVertices.get(17));

        grafo.DijkstraShortestPath(listaVertices.get(8), listaVertices.get(12));

        grafo.DijkstraShortestPath(listaVertices.get(3), listaVertices.get(9));
    }
}
