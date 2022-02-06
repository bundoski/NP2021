package firstMidterm;

import java.util.*;

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