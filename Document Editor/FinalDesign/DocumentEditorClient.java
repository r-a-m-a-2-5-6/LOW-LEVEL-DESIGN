package FinalDesign;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

interface DocumentElement{
    public abstract String render();
}

class TextElement implements DocumentElement{

    private String text;

    public TextElement(String text){
        this.text = text;
    }
    @Override 
    public String render(){
        return text;
    }
}

class ImageElement implements DocumentElement{
    private String imagePath;

    public ImageElement(String path){
        this.imagePath = path;
    }

    @Override 
    public String render(){
        return "Image:-"+imagePath+" ";
    }
}

class NewLineElement implements DocumentElement{
    

    @Override 
    public String render(){
        return "\n";
    }
}

class TabSpaceElement implements DocumentElement{
    

    @Override 
    public String render(){
        return "\t";
    }
}


class Document{

    List<DocumentElement> document = new ArrayList<>();

    public void addElement(DocumentElement element){
        document.add(element);
    }

    public String renderDocument(){
        StringBuilder result  = new StringBuilder();
        for(DocumentElement element : document){
            result.append(element.render());
        }

        return result.toString();
    }
}


interface Storage{

    void save(String data);
}

class SaveToFile implements Storage{



    @Override 
    public void save(String data){
        try {
            FileWriter writer = new FileWriter("document.txt");
            writer.write(data);
            writer.close();
            System.out.println("Data saved to file sucessfuly");
        } catch (Exception e) {
            System.out.println("Unable to open the file");
        }
    }
}

class DocumentEditor{

    private Document document;
    private Storage storage;

    private String renderDocument = "";

    public DocumentEditor(Document document,Storage storage){
        this.document = document;
        this.storage = storage;
    }

    public void addText(String text){
        document.addElement(new TextElement(text));
    }

    public void addImage(String imagePath){
        document.addElement(new ImageElement(imagePath));
    }

    public void addNewLine(){
        document.addElement(new NewLineElement());
    }

    public void addTabSpace(){
        document.addElement(new TabSpaceElement());
    }

    public String  render(){
        if (renderDocument.isEmpty()){
            renderDocument = document.renderDocument();
        }

        return renderDocument;
    }

    public void save(){
        storage.save(document.renderDocument());
    }

}

public class DocumentEditorClient {
    public static void main(String[] args) {
        Document document = new Document();
        SaveToFile storage = new SaveToFile();

        DocumentEditor doc = new DocumentEditor(document,storage);

        doc.addImage("image.png");
        doc.addNewLine();
        doc.addTabSpace();
        doc.addText("Hey this is siva rama krishna");

        System.out.println(doc.render());

        doc.save();
    }
}
