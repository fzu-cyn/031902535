package com.company;

import java.io.*;
import java.util.*;

public class Check {
    // public Check(){}
    public static List<output> list = new ArrayList<output>();
    //定义集合List来保存output对象
    public static long sum = 0;//敏感词总数
    public static long line = 1;//行数
    private Map Mapss = null;
    public static int min = 1;//最小匹配规则
    public static int max = 2;//最大匹配规则
    private static final String ENCODING = "utf-8";

    private static String replaceString = null;

    public static String readToString(String fileName) throws IOException {
        //String encoding = "UTF-8";
        File filetxt = new File(fileName);
        Long filelength = filetxt.length();
        byte[] filecontent = new byte[filelength.intValue()];
        //System.out.println();
        FileInputStream in = new FileInputStream(filetxt);
        try {
            in.read(filecontent);
            return new String(filecontent, ENCODING);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            in.close();
        }
    }//实现一次性读入待检测文本
     public static void main(String[] args) throws IOException {
         Check Sensitive = new Check();
       //  System.out.println("敏感词的数量：" + Sensitive.Mapss.size());
         //读入待检测文本
        //String txt = readToString("C:\\Users\\陈玉娜\\IdeaProjects\\Filter\\org.txt");
        //Set<String> word = Sensitive.getWord(txt,1);//获取文本中的敏感词

         try {
             BufferedReader br = new BufferedReader(
                     new InputStreamReader(
                             new FileInputStream("C:\\Users\\陈玉娜\\IdeaProjects\\Filter\\org.txt")));
             String linestr;//按行读取 将每次读取一行的结果赋值给linestr
             while ((linestr = br.readLine()) != null) {
                 Set<String> word = Sensitive.getWord(linestr,1);//获取文本中的敏感词
                 //System.out.println(word);
                 line++;//行数增加
             }
             br.close();//关闭IO
         } catch (Exception e) {
             System.out.println("文件操作失败");
             e.printStackTrace();
         }
         Collections.sort(list);
        // 输出到文本
         File fileout = new File("C:\\Users\\陈玉娜\\IdeaProjects\\Filter\\ans.txt");//修改后缀可生成相应类型文件
         try{
             if(!fileout.exists()) fileout.createNewFile();//文件不存在，创建文件
             BufferedWriter bw = new BufferedWriter(new FileWriter(fileout));
             String str = "Total:"+sum+'\n';
             bw.write(str);
             char str3 = '\n';
             for(int i=0;i<sum;i++){
                 bw.write(list.get(i).toString());
                 bw.write(str3);
             }
             bw.close();
         }catch(IOException e){
             e.printStackTrace();
     }
    }
     //构造函数，初始化敏感词词库
    public Check(){
        Mapss = new Filter().lexicon();
    }

    //▲实现输出敏感词+所在行数
    //获取文本中的敏感词，最小匹配规则为1，最大匹配规则为2
    public Set<String> getWord(String txt,int type){
        Set<String> wordlist = new HashSet<String>();
        for (int i=0;i<txt.length();i++){
            //判断是否包含敏感词
            int len = search(txt,i,type);
            if(len>0){
                //如果存在，加入wordlist
                list.add(new output(line,i, txt.substring(i,i+len),txt.substring(i,i+len)));
                //System.out.println("Sum"+sum);
                //System.out.println(txt.substring(i,i+len));//敏感词
                wordlist.add(txt.substring(i,i+len));
                i=i+len-1;
            }
        }
        return wordlist;
    }

    //检查文本中是否包含敏感词
    public int search(String txt,int start,int type){
        boolean leaf = false;//敏感词结束标识位：用于敏感词只有1位的情况
        int flag = 0;//匹配标识数默认为0
        char sensitiveword = 0;
        Map New =Mapss;
        for(int i=start;i<txt.length();i++){
            sensitiveword = txt.charAt(i);
            New = (Map)New.get(sensitiveword);//获取指定敏感词
            if(New!=null){
                flag++;
                if("1".equals(New.get("isEnd"))){
                    leaf = true;
                    sum++;
                    //System.out.println(sensitiveword);
                    if(Check.min==type) break;
                }
            }
            else break;
        }
        if(flag<2||!leaf) flag=0;
        return flag;
    }

    public class output implements Comparable<output> {
        private long linenumber;//行数
        private int location;//位置
        private String words1;//词库敏感词
        private String words2;//文本敏感词
        public output(long linenumber,int location,String words1,String words2)
        { //利用构造方法初始化成员字段
            this.linenumber=linenumber;
            this.location=location;
            this.words1=words1;
            this.words2=words2;
        }@Override
        public int compareTo(output o)
        {//利用编号实现对象间的比较
            if(linenumber > o.linenumber) {
                return 1;
            }else if(linenumber < o.linenumber) {
                return -1;
            } return 0;
        }

        @Override
        public String toString() {  //重写toString（）方法
            StringBuilder t = new StringBuilder();
            t.append("Line" + linenumber);
            t.append(": <" + words1);
            t.append("> " + words2 );
            return t.toString();
        }

        public int getLocation() {
            return location;
        }

        public void setLocation(int location) {
            this.location = location;
        }

        public void setLinenumber(long linenumber) {
            this.linenumber = linenumber;
        }

        public void setWords1(String words1) {
            this.words1 = words1;
        }

        public void setWords2(String words2) {
            this.words2 = words2;
        }

        public long getLinenumber() {
            return linenumber;
        }

        public String getWords1() {
            return words1;
        }

        public String getWords2() {
            return words2;
        }
    }
}
