package auds.Aud10DesignPatterns.Composite;

import java.nio.file.FileSystem;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


interface IFile {         // IFile go pretstavuva Component delo od slikata.
    String getFileName();
    long getFileSize();
    String getFileInfo(int indent);
    void sortyBySize();
    long findLargestFile();
}

class File implements IFile {   // obicna datoteka, list vo delo od slikata.
    String name;
    long size;

    public File(String name, long size) {
        this.name = name;
        this.size = size;
    }
// samo gi implementirame metodite od interfejso
    @Override
    public String getFileName() {
        return name;
    }

    @Override
    public long getFileSize() {
        return size;
    }

    @Override
    public String getFileInfo(int indent) {
        StringBuilder indentString = new StringBuilder();
        for( int i=0; i<indent;i++){
            indentString.append("\t");
        }
        return String.format("File name %10s File size: %10d", name, size);
    }

    @Override
    public void sortyBySize() {
        // Do NOTHING, ova e Fajlot, ne e Folder / Direktorium.
    }

    @Override
    public long findLargestFile() {
         return size;
    }
}

class Folder implements IFile{

    String name;
    List<IFile> children;

    public Folder(String name){
        this.name = name;
        children = new ArrayList<>();
    }

    @Override
    public String getFileName() {
        return name;
    }

    @Override
    public long getFileSize() {  // rekurzivno ce gi izminime site deca
        return children.stream().mapToLong(IFile::getFileSize).sum();
        // ova e rekurzivnoto izminuvanje na direktoriumo.
    }

    @Override
    public String getFileInfo(int indent) {
        StringBuilder indentString = new StringBuilder();
        for( int i=0; i<indent;i++){
            indentString.append("\t");
        }
        StringBuilder finalSb = new StringBuilder();
        finalSb.append(String.format("%sFile name %10s File size: %10d", indentString.toString(), name, getFileSize()));
        children.forEach(child -> finalSb.append(child.getFileInfo(indent+1)));
        return finalSb.toString();
    }

    @Override
    public void sortyBySize() {

    }

    @Override
    public long findLargestFile() {
        return 0;
    }

    public void addFile(IFile file) {
        children.add(file);
    }
}


