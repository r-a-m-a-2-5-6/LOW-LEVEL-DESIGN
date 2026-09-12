package IntialDesign;


import java.io.FileWriter;
import java.util.*;

class DocumentEditor{

    
    private List<String> elements;
    private String  renderDocument;

    //constructor
    public DocumentEditor(){
        elements = new ArrayList<>();

        renderDocument = "";
    }

    //Add text function
    public void addText(String text){
        elements.add(text);
    }

    //Add image
    public void addImage(String imagePathString){
        elements.add("Image :- {imagePathString}");
    }

    //render Document
    public String renderDocuments(){

        if(renderDocument.isEmpty()){
            StringBuilder str = new StringBuilder();

            for(String element : elements){

                if(element.length() > 4 && 
                    (element.endsWith(".jpg") || element.endsWith(".png"))){
                        str.append("Image :-").append(element).append("\n");
                }else{
                    str.append(element).append("\n");
                }
            }

            renderDocument = str.toString();
        }

        return renderDocument;
    }


    //save to file function
    public void saveToFile(){
        try {
            FileWriter writer = new FileWriter("document.txt");
            writer.write(renderDocuments());
            writer.close();
            System.out.println("file saved sucessFully");
        } catch (Exception e) {
            System.out.println("Unable to open the file");
        }
    }
    
   
}

public class DocumentEditorClient{

    public static void main(String[] args) {
        System.out.println("Hit ");
        DocumentEditor document= new DocumentEditor();

        document.addText("Hi this is siva rama krishna");
        document.addImage("photo.jpg");
        document.renderDocuments();
        document.saveToFile();
    }
}
