import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
public class HuffmanCode{
    //建立数的节点类
    static class Node{
        int weight;//频数
        int parent;
        int leftChild;
        int rightChild;
        //构造函数
        public Node(int weight,int parent,int leftChild,int rightChild){
            this.weight=weight;
            this.parent=parent;
            this.leftChild=leftChild;
            this.rightChild=rightChild;
        }

        void setWeight(int weight){
            this.weight=weight;
        }

        void setParent(int parent){
            this.parent=parent;
        }

        void setLeftChild(int leftChild){
            this.leftChild=leftChild;
        }

        void setRightChild(int rightChild){
            this.rightChild=rightChild;
        }

        int getWeight(){
            return weight;
        }

        int getParent(){
            return parent;
        }

        int getLeftChild(){
            return leftChild;
        }

        int getRightChild(){
            return rightChild;
        }
    }

    //新建哈夫曼编码
    static class NodeCode{
        String character;
        String code;
        NodeCode(String character,String code){
            this.character=character;
            this.code=code;
        }
        NodeCode(String code){
            this.code= code;
        }

        void setCharacter(String character){
            this.character=character;
        }

        void setCode(String code){
            this.code=code;
        }

        String getCharacter(){
            return character;
        }

        String getCode(){
            return code;
        }
    }

    //初始化一个huffman树
    public static void initHuffmanTree(Node[] huffmanTree,int m){
        for(int i=0;i<m;i++){//m为需要创建的节点数，m=2*n-1
            huffmanTree[i] = new Node(0,-1,-1,-1);
        }
    }

    //初始化一个huffmanCode
    public static void initHuffmanCode(NodeCode[] huffmanCode,int n){
        for(int i=0;i<n;i++){//有多少个字符
            huffmanCode[i]=new NodeCode("","");
        }
    }

    //获取huffmanCode的符号
    public static void getHuffmanCode(NodeCode[] huffmanCode , int n, String file) throws IOException {
        HashMap<Character,Integer> map = Get.get_char_num(file);
        int i = 0;
        for (Character key : map.keySet()) {
            huffmanCode[i] = new NodeCode(String.valueOf(key),"");
            i++;
        }
    }

    //获取huffman树节点频数
    public static void getHuffmanWeight(Node[] huffmanTree , int n, String file) throws IOException {
        HashMap<Character,Integer> map = Get.get_char_num(file);
        int i = 0;
        for (Integer value : map.values()){
            huffmanTree[i] = new Node(value,-1,-1,-1);
            i++;
        }
    }

    //从n个结点中选取最小的两个结点
    //返回最小的两个数的位置
    public static int[] selectMin(Node[] huffmanTree ,int n)
    {
        int min[] = new int[2];
        class TempNode
        {
            int newWeight;//存储权
            int place;//存储该结点所在的位置

            TempNode(int newWeight,int place){
                this.newWeight=newWeight;
                this.place=place;
            }

            void setNewWeight(int newWeight){
                this.newWeight=newWeight;
            }

            void setPlace(int place){
                this.place=place;
            }

            int getNewWeight(){
                return newWeight;
            }

            int getPlace(){
                return place;
            }
        }

        TempNode[] tempTree=new TempNode[n];

        //将huffmanTree中没有双亲的结点存储到tempTree中
        //第一次都没有双亲，还未构造编码树
        int i=0,j=0;
        for(i=0;i<n;i++)
        {
            if(huffmanTree[i].getParent()==-1&& huffmanTree[i].getWeight()!=0)
            {
                tempTree[j]= new TempNode(huffmanTree[i].getWeight(),i);
                j++;
            }
        }

        int m1,m2;
        m1=m2=0;
        //遍历所有的结点找到最小的两个数
        for(i=0;i<j;i++)
        {
            if(tempTree[i].getNewWeight()<tempTree[m1].getNewWeight())//此处不让取到相等，是因为结点中有相同权值的时候，m1取最前的
                m1=i;
        }
        for(i=0;i<j;i++)
        {
            if(m1==m2)
                m2++;//当m1在第一个位置的时候，m2向后移一位
            if(tempTree[i].getNewWeight()<=tempTree[m2].getNewWeight()&& i!=m1)//此处取到相等，是让在结点中有相同的权值的时候，

                //m2取最后的那个。
                m2=i;
        }

        min[0]=tempTree[m1].getPlace();
        min[1]=tempTree[m2].getPlace();
        return min;//返回在数组中的位置
    }

    //创建huffmanTree
    public static void createHuffmanTree(Node[] huffmanTree,int n){
        if(n<=1)
            System.out.println("Parameter Error!");
        int m = 2*n-1;//节点数
        //initHuffmanTree(huffmanTree,m);

        for(int i=n;i<m;i++)
        {
            int[] min=selectMin(huffmanTree,i);
            int min1=min[0];
            int min2=min[1];
            //构造一个树，设置位置
            huffmanTree[min1].setParent(i);
            huffmanTree[min2].setParent(i);
            huffmanTree[i].setLeftChild(min1);
            huffmanTree[i].setRightChild(min2);
            //为子树的频数之和
            huffmanTree[i].setWeight(huffmanTree[min1].getWeight()+ huffmanTree[min2].getWeight());
        }
    }

    //创建huffmanCode
    //哈夫曼码
    public static void createHuffmanCode(Node[] huffmanTree,NodeCode[] huffmanCode,int n){
        char[] code = new char[100];
        int start;
        int c;
        int parent;
        code[n-1]='0';
        //在tree中0~n-1中全是叶子结点
        for(int i=0;i<n;i++)
        {
            StringBuilder stringBuffer = new StringBuilder();
            start=n-1;
            c=i;
            //自底向上，倒过来赋值
            while( (parent=huffmanTree[c].getParent()) >=0 )
            {
                start--;
                code[start]=((huffmanTree[parent].getLeftChild()==c)?'0':'1');
                c=parent;

            }
            //将code数组添加到stringBuffer中
            for(;start<n-1;start++){
                stringBuffer.append(code[start]);
            }
            //添加到huffmanCode中
            huffmanCode[i].setCode(stringBuffer.toString());
        }
    }
    //清空文件
    public static void clearInfoForFile(String fileName) {
        File file =new File(fileName);
        try {
            if(!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWriter =new FileWriter(file);
            fileWriter.write("");
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //输出huffmanCode
    public static void outputHuffmanCode(HashMap map,NodeCode[] huffmanCode,int n,String outputfile) throws IOException {
        BufferedWriter ot = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputfile,true),"UTF-8"));
        //清空原来的文件
        clearInfoForFile(outputfile);
        for(int i=0;i<n;i++){
            ot.write(huffmanCode[i].getCharacter()+":"+huffmanCode[i].getCode()+"  频数为："+map.get((huffmanCode[i].getCharacter()).charAt(0))+"\n");
        }
        ot.close();
    }
    //压缩编码
    public static void encode(String file, String encode_file, int n,NodeCode[] huffmanCode) throws IOException {
        //将字符和code的关系存入新的map里面
        HashMap<String,String> map = new HashMap<>();
        for (int i=0;i<n;i++){
            map.put(huffmanCode[i].getCharacter(),huffmanCode[i].getCode());
        }
        //读文件
        FileReader fr = new FileReader(file);
        //一个字符一个字符地读
        char[] ch = new char[1];
        //清空原来的文件
        clearInfoForFile(encode_file);
        //输出
        BufferedWriter output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(encode_file,true), StandardCharsets.UTF_8));
        while (fr.read(ch)!=-1){
            if (ch[0] == '\n')
                output.write(map.get(String.valueOf(ch[0])) + "\n");
            output.write( map.get(String.valueOf(ch[0])) + " ");
        }
        output.close();
    }
    //解码函数
    public static void decode(String decode_file, String encode_file,NodeCode[] huffmanCode,int n) throws IOException {
        //将char 和 huffmancode的关系存入新的map
        HashMap<String,String> map = new HashMap<>();
        for (int i=0;i<n;i++){
            map.put(huffmanCode[i].getCode(),huffmanCode[i].getCharacter());
        }
        //清空原来的文件
        clearInfoForFile(decode_file);
        //读编码文件
        FileReader fr = new FileReader(encode_file);
        //一个字符一个字符地读
        char[] ch = new char[1];
        //输出到解码文件
        BufferedWriter output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(decode_file,true), StandardCharsets.UTF_8));
        //建立一个String
        StringBuilder Str = new StringBuilder();
        while (fr.read(ch)!=-1){
            if (ch[0]!=' '&&ch[0]!='\n'){
                Str.append(ch[0]);
            }
            else{
                output.write(map.get(Str.toString()));
                Str.delete(0, Str.length());
            }
        }
        output.close();
    }
    //主函数
    public static void main(String[] args) throws IOException {
        //文本地址
        String File = "D:\\IdeaProjects\\Huffman\\HuffmanTree\\src\\f1.txt";
        //输出文件地址和名字
        String outputfile = "D:\\IdeaProjects\\Huffman\\HuffmanTree\\src\\f1_code.txt";
        int n;
        int m;
        //通过构建哈希表来统计字符种类和频数
        HashMap<Character, Integer> Map = Get.get_char_num(File);
        n = Map.size();
        //m为结点数
        m=2*n-1;
        Node[] huffmanTree = new Node[m];
        NodeCode[] huffmanCode = new NodeCode[n];

        //初始化huffmanTree,huffmanCode
        initHuffmanTree(huffmanTree,m);
        initHuffmanCode(huffmanCode,n);

        //获取huffmanCode的符号
        getHuffmanCode(huffmanCode,n,File);

        //获取huffmanTree的频数
        getHuffmanWeight(huffmanTree,n,File);

        //创建huffmanTree
        createHuffmanTree(huffmanTree,n);
        //创建huffmanCode
        createHuffmanCode(huffmanTree,huffmanCode,n);

        //输出huffmanCode编码
        outputHuffmanCode(Map,huffmanCode,n,outputfile);

        //压缩编码
        String encode_file = "D:\\IdeaProjects\\Huffman\\HuffmanTree\\src\\f1_encode.txt";
        encode(File,encode_file,n,huffmanCode);

        //解压
        String decode_file = "D:\\IdeaProjects\\Huffman\\HuffmanTree\\src\\f1_decode.txt";
        decode(decode_file,encode_file,huffmanCode,n);
    }
}
