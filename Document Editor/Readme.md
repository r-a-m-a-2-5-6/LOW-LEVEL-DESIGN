# Document Editor — Low-Level Design

A simple **Document Editor** designed using object-oriented principles and basic **Low-Level Design (LLD)** concepts.

The project demonstrates how to design a document editor that can handle different types of document elements such as **text, images, new lines, and tab spaces**, while keeping document rendering and storage loosely coupled.

## 🧩 Design / Whiteboard

The complete design and class relationships are available on Excalidraw:

[Document Editor LLD — Excalidraw](https://excalidraw.com/?utm_source=chatgpt.com#json=pBP1StFU14381SHdnVXqi,Ij3Cfd6NmixibWldcfUpDA)

---

# 📌 Problem Statement

Design a document editor that supports:

* Adding text
* Adding images
* Adding new lines
* Adding tab spaces
* Rendering the complete document
* Saving the document to storage

The design should be extensible so that adding a new type of document element or a new storage mechanism does not require major changes to existing classes.

---

# 🏗️ High-Level Design

The system is divided into three major responsibilities:

```text
                    DocumentEditorClient
                             |
                             v
                    +----------------+
                    | DocumentEditor |
                    +----------------+
                       /           \
                      v             v
              +-----------+     +----------+
              | Document  |     | Storage  |
              +-----------+     +----------+
                    |                 |
                    v                 v
          DocumentElement         SaveToFile
                    |
        +-----------+-----------+
        |           |           |
        v           v           v
     TextElement ImageElement NewLineElement
                               
                         + TabSpaceElement
```

---

# 📦 Main Components

## 1. `DocumentElement`

```java
interface DocumentElement {
    String render();
}
```

`DocumentElement` is the common abstraction for everything that can exist inside a document.

Every document element is responsible for knowing **how it should render itself**.

Examples:

* Text
* Image
* New line
* Tab space

This avoids putting type-checking logic inside `Document`.

---

# 2. `TextElement`

```java
class TextElement implements DocumentElement
```

Represents text inside the document.

Example:

```java
doc.addText("Hello World");
```

Internally:

```java
new TextElement("Hello World")
```

When rendered:

```text
Hello World
```

---

# 3. `ImageElement`

```java
class ImageElement implements DocumentElement
```

Represents an image using its path.

Example:

```java
doc.addImage("image.png");
```

Rendering produces:

```text
Image:-image.png
```

The `Document` does not need to know how an image is rendered.

It simply calls:

```java
element.render();
```

---

# 4. `NewLineElement`

```java
class NewLineElement implements DocumentElement
```

Represents a new line.

```java
doc.addNewLine();
```

Its rendering is:

```text
"\n"
```

---

# 5. `TabSpaceElement`

```java
class TabSpaceElement implements DocumentElement
```

Represents a tab space.

```java
doc.addTabSpace();
```

Its rendering is:

```text
"\t"
```

---

# 📄 Document

```java
class Document {

    List<DocumentElement> document = new ArrayList<>();

    public void addElement(DocumentElement element) {
        document.add(element);
    }

    public String renderDocument() {
        StringBuilder result = new StringBuilder();

        for (DocumentElement element : document) {
            result.append(element.render());
        }

        return result.toString();
    }
}
```

`Document` maintains the collection of document elements.

It does **not** need to know whether an element is:

* Text
* Image
* New line
* Tab space

It simply works with the abstraction:

```java
DocumentElement
```

The rendering flow is:

```text
Document
   |
   |-- TextElement
   |      └── render()
   |
   |-- ImageElement
   |      └── render()
   |
   |-- NewLineElement
   |      └── render()
   |
   └-- TabSpaceElement
          └── render()
```

This is an example of **polymorphism**.

---

# 💾 Storage

The storage responsibility is separated using the `Storage` interface.

```java
interface Storage {
    void save(String data);
}
```

The interface represents the abstraction for storing the document.

Currently, the project provides:

```java
class SaveToFile implements Storage
```

which saves the document into:

```text
document.txt
```

---

# Why `Storage` Is an Interface

Without the `Storage` abstraction, `DocumentEditor` would have to directly depend on file handling.

For example:

```java
class DocumentEditor {

    public void save() {
        FileWriter writer = new FileWriter("document.txt");
    }
}
```

Now `DocumentEditor` is tightly coupled to file storage.

If we later want to save to:

* Database
* Cloud storage
* AWS S3
* Google Drive
* Another file
* Remote API

we would have to modify `DocumentEditor`.

Instead, we use:

```java
interface Storage {
    void save(String data);
}
```

and:

```java
class SaveToFile implements Storage {
    ...
}
```

Now `DocumentEditor` depends on the abstraction rather than a specific storage implementation.

---

# 🧠 DocumentEditor

`DocumentEditor` acts as the main coordinator/facade for editing the document.

```java
class DocumentEditor {

    private Document document;
    private Storage storage;
}
```

It provides operations such as:

```java
addText()
addImage()
addNewLine()
addTabSpace()
render()
save()
```

Example:

```java
doc.addImage("image.png");
doc.addNewLine();
doc.addTabSpace();
doc.addText("Hey this is siva rama krishna");
```

The client does not need to directly create every `DocumentElement`.

Instead:

```text
Client
  |
  v
DocumentEditor
  |
  +----> Document
  |
  +----> Storage
```

---

# 🔄 Rendering Flow

When the client calls:

```java
doc.render();
```

the flow is:

```text
DocumentEditor
       |
       v
Document.renderDocument()
       |
       v
Iterate over DocumentElement
       |
       +----> TextElement.render()
       |
       +----> ImageElement.render()
       |
       +----> NewLineElement.render()
       |
       +----> TabSpaceElement.render()
       |
       v
StringBuilder
       |
       v
Final rendered document
```

For example:

```java
doc.addImage("image.png");
doc.addNewLine();
doc.addTabSpace();
doc.addText("Hello");
```

The rendered result is conceptually:

```text
Image:-image.png
    Hello
```

---

# 💾 Saving Flow

When:

```java
doc.save();
```

is called:

```text
DocumentEditor
       |
       v
document.renderDocument()
       |
       v
String data
       |
       v
Storage.save(data)
       |
       v
SaveToFile
       |
       v
document.txt
```

This keeps **document editing** separate from **storage**.

---

# 🎯 Design Principles Used

## 1. Single Responsibility Principle — SRP

Different classes have different responsibilities.

| Class             | Responsibility                               |
| ----------------- | -------------------------------------------- |
| `TextElement`     | Represents and renders text                  |
| `ImageElement`    | Represents and renders an image              |
| `NewLineElement`  | Represents a new line                        |
| `TabSpaceElement` | Represents tab spacing                       |
| `Document`        | Maintains document elements and renders them |
| `SaveToFile`      | Saves data to a file                         |
| `DocumentEditor`  | Coordinates document editing and storage     |

A class should have one primary reason to change.

---

## 2. Open/Closed Principle — OCP

The design is open for extension but closed for modification.

Suppose we want to add:

```java
class VideoElement implements DocumentElement {
    @Override
    public String render() {
        return "Video";
    }
}
```

We don't need to modify `Document`.

We can simply add:

```java
document.addElement(new VideoElement());
```

Similarly, a new storage implementation can be added:

```java
class DatabaseStorage implements Storage {
    @Override
    public void save(String data) {
        // save to database
    }
}
```

`DocumentEditor` doesn't need to change.

---

## 3. Dependency Inversion Principle — DIP

`DocumentEditor` depends on:

```java
Storage
```

rather than:

```java
SaveToFile
```

This is important.

Instead of:

```java
private SaveToFile storage;
```

we have:

```java
private Storage storage;
```

Therefore the high-level component does not depend directly on a low-level implementation.

---

## 4. Polymorphism

`Document` stores:

```java
List<DocumentElement>
```

rather than:

```java
List<TextElement>
List<ImageElement>
...
```

Every element implements:

```java
DocumentElement
```

Therefore we can simply do:

```java
for (DocumentElement element : document) {
    result.append(element.render());
}
```

The actual implementation of `render()` is decided at runtime.

---

# 🔌 Extensibility

The design makes it easy to add new document elements.

For example:

### Video

```java
class VideoElement implements DocumentElement {

    private String videoPath;

    public VideoElement(String videoPath) {
        this.videoPath = videoPath;
    }

    @Override
    public String render() {
        return "Video:-" + videoPath;
    }
}
```

### Link

```java
class LinkElement implements DocumentElement {

    private String url;

    public LinkElement(String url) {
        this.url = url;
    }

    @Override
    public String render() {
        return "Link:-" + url;
    }
}
```

No changes are required inside the `Document.renderDocument()` method.

---

# 🔄 Extending Storage

Currently:

```text
Storage
   |
   └── SaveToFile
```

We can extend it to:

```text
              Storage
             /       \
            /         \
     SaveToFile   DatabaseStorage
                       |
                  CloudStorage
```

For example:

```java
class DatabaseStorage implements Storage {

    @Override
    public void save(String data) {
        // Save document to database
    }
}
```

Then:

```java
Storage storage = new DatabaseStorage();

DocumentEditor editor =
    new DocumentEditor(document, storage);
```

`DocumentEditor` remains unchanged.

---

# 🚀 Example Usage

```java
public class DocumentEditorClient {

    public static void main(String[] args) {

        Document document = new Document();

        SaveToFile storage = new SaveToFile();

        DocumentEditor doc =
            new DocumentEditor(document, storage);

        doc.addImage("image.png");

        doc.addNewLine();

        doc.addTabSpace();

        doc.addText("Hey this is siva rama krishna");

        System.out.println(doc.render());

        doc.save();
    }
}
```

---

# 📁 Project Structure

```text
Document Editor/
│
├── FinalDesign/
│   └── DocumentEditorClient.java
│
├── document.txt
│
└── README.md
```

The current implementation keeps the classes together for simplicity. In a larger application, each class/interface can be moved into its own file.

For example:

```text
FinalDesign/
│
├── DocumentEditorClient.java
├── DocumentEditor.java
├── Document.java
├── DocumentElement.java
├── TextElement.java
├── ImageElement.java
├── NewLineElement.java
├── TabSpaceElement.java
├── Storage.java
└── SaveToFile.java
```

---

# ▶️ How to Run

Navigate to the project directory:

```bash
cd "Document Editor"
```

Compile:

```bash
javac FinalDesign/DocumentEditorClient.java
```

Run:

```bash
java FinalDesign.DocumentEditorClient
```

The rendered document will be printed to the console and the document will be saved to:

```text
document.txt
```

---

# 📚 LLD Concepts Demonstrated

This project demonstrates:

* Object-Oriented Programming
* Interfaces
* Abstraction
* Encapsulation
* Polymorphism
* Composition
* Dependency Injection
* Single Responsibility Principle
* Open/Closed Principle
* Dependency Inversion Principle
* Loose Coupling
* Separation of Concerns
* Extensibility
* Basic Facade/Coordinator-style design

---

# 🔮 Possible Future Improvements

The design can be extended with:

* Undo / Redo
* Copy / Paste
* Multiple document formats
* PDF storage
* Database storage
* Cloud storage
* Rich text formatting
* Bold / Italic / Underline
* Hyperlinks
* Tables
* Video elements
* Document versioning
* Auto-save
* Multiple rendering formats
* Different output formats such as HTML/Markdown/PDF

---

# 🎓 Key LLD Takeaway

The main lesson from this design is:

> **Depend on abstractions and give each class a clear responsibility.**

Instead of creating one large `DocumentEditor` class that handles:

```text
Text
Image
Formatting
Rendering
File handling
Database handling
```

we separate these responsibilities into independent components.

This makes the system:

```text
Easier to understand
        ↓
Easier to test
        ↓
Easier to extend
        ↓
Easier to maintain
        ↓
Less tightly coupled
```

The design can therefore evolve without constantly modifying existing, working code.
