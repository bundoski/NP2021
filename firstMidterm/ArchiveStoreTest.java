package firstMidterm;

import java.util.*;


/*
Да се имплементира класа ArchiveStore во која се чува листа на архиви (елементи за архивирање).

Секој елемент за архивирање Archive има:

id - цел број
dateArchived - датум на архивирање.

Постојат два видови на елементи за архивирање, LockedArchive за кој дополнително се чува датум до кој не смее да се отвори dateToOpen и SpecialArchive за 
кој се чуваат максимален број на дозволени отварања maxOpen. За елементите за архивирање треба да се обезбедат следните методи:

LockedArchive(int id, Date dateToOpen) - конструктор за заклучена архива
SpecialArchive(int id, int maxOpen) - конструктор за специјална архива
За класата ArchiveStore да се обезбедат следните методи:

ArchiveStore() - default конструктор
void archiveItem(Archive item, Date date) - метод за архивирање елемент item на одреден датум date
void openItem(int id, Date date) - метод за отварање елемент од архивата со зададен id и одреден датум date. Ако не постои елемент со даденото id треба да 
се фрли исклучок од тип NonExistingItemException со порака Item with id [id] doesn't exist.
String getLog() - враќа стринг со сите пораки запишани при архивирањето и отварањето архиви во посебен ред.
За секоја акција на архивирање во текст треба да се додаде следната порака Item [id] archived at [date], додека за секоја акција на отварање архива треба да се додаде Item [id] opened at [date].
При отварање ако се работи за LockedArhive и датумот на отварање е 
пред датумот кога може да се отвори, да се додаде порака Item [id] cannot be opened before [date]. Ако се работи за SpecialArhive и се обидиеме да ја отвориме 
повеќе пати од дозволениот број (maxOpen) да се додаде порака Item [id] cannot be opened more than [maxOpen] times.
*/

class NonExistingItemException extends Exception {
    NonExistingItemException(int id) {
        super("Item with id " + id + " doesn't exist");
    }
}

public class ArchiveStoreTest {

    public static void main(String[] args) {
        ArchiveStore store = new ArchiveStore();
        Date date = new Date(113, 10, 7);
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        int n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        int i;
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            long days = scanner.nextLong();
            Date dateToOpen = new Date(date.getTime() + (days * 24 * 60
                    * 60 * 1000));
            LockedArchive lockedArchive = new LockedArchive(id, dateToOpen);
            store.archiveItem(lockedArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            int maxOpen = scanner.nextInt();
            SpecialArchive specialArchive = new SpecialArchive(id, maxOpen);
            store.archiveItem(specialArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        while (scanner.hasNext()) {
            int open = scanner.nextInt();
            try {
                store.openItem(open, date);
            } catch (NonExistingItemException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println(store.getLog());
    }
}

class ArchiveStore {

    List<Archive> archiveList;
    StringBuilder log;

    public ArchiveStore() {
        archiveList = new ArrayList<Archive>();
        log = new StringBuilder();
    }

    void archiveItem(Archive item, Date date) {
        TimeZone.setDefault( TimeZone.getTimeZone("UTC"));
        item.setDate(date);
        archiveList.add(item);
        log.append("Item " + item.getId() + " archived at " + date + "\n");
    }

    void openItem(int id, Date date) throws NonExistingItemException {
        if (archiveList.stream().noneMatch(item -> item.getId() == id))
            throw new NonExistingItemException(id);
        else {
            TimeZone.setDefault( TimeZone.getTimeZone("UTC"));
            Archive archive = archiveList.stream().filter(item -> item.getId() == id).findFirst().get();
            if (archive.getClass().getSimpleName().equals("LockedArchive")) {
                LockedArchive tempArchive = (LockedArchive) archive;
                if (tempArchive.getDateToOpen().compareTo(date) > 0)
                    log.append("Item " + tempArchive.getId() + " cannot be opened before " + tempArchive.getDateToOpen() + "\n");
                else
                    log.append("Item " + tempArchive.getId() + " opened at " + date + "\n");
            } else {
                SpecialArchive tempArchive = (SpecialArchive) archive;
                if (tempArchive.canBeOpened()) {
                    log.append("Item " + tempArchive.getId() + " opened at " + date + "\n");
                    tempArchive.increaseCounter();
                } else {
                    log.append("Item " + tempArchive.getId() + " cannot be opened more than " + tempArchive.getMaxOpen() + " times\n");
                }
            }
        }
    }

    public String getLog() {
        return log.toString();
    }

}

class Archive {
    // TODO - shto ako se vnesat dve arhivi so ist id?
    private int id;
    private Date dateArchived;

    public Archive(int id) {
        this.id = id;
    }

    public void setDate(Date date) {
        this.dateArchived = date;
    }

    public int getId() {
        return id;
    }

}

class LockedArchive extends Archive {
    private Date dateToOpen;

    LockedArchive(int id, Date dateToOpen) {
        super(id);
        this.dateToOpen = dateToOpen;
    }

    public Date getDateToOpen() {
        return dateToOpen;
    }

}

class SpecialArchive extends Archive {

    private int maxOpen;
    private int counterOpen;

    public SpecialArchive(int id, int maxOpen) {
        super(id);
        this.maxOpen = maxOpen;
    }

    public boolean canBeOpened() {
        return maxOpen > counterOpen;
    }

    public void increaseCounter() {
        counterOpen++;
    }

    public int getMaxOpen() {
        return maxOpen;
    }
}
