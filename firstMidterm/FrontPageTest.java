package firstMidterm;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

class Category{
    String nameOfCategory;

    public Category(String nameOfCategory){
        this.nameOfCategory = nameOfCategory;
    }

    public String getName(){
        return nameOfCategory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(nameOfCategory, category.nameOfCategory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nameOfCategory);
    }

}

abstract class NewsItem{
    String title;
    Date date;
    Category category;

    public NewsItem(String title, Date date, Category category){
        this.title = title;
        this.date = date;
        this.category = category;
    }

    public Category getCategory(){
        return category;
    }

    public abstract String getTeaser();
}

class TextNewsItem extends NewsItem{
    String text;
    public TextNewsItem(String title, Date date, Category category, String text){
        super(title, date, category);
        this.text = text;
    }


    @Override
    public String getTeaser() {
        return String.format("%s\n%s\n%s\n", title,
                Math.abs(date.getMinutes()- LocalDateTime.now().getMinute()) + 60 * Math.abs(date.getHours()-LocalDateTime.now().getHour()),
                text.length()>=80 ? text.substring(0, 80):text);
    }
}
class MediaNewsItem extends NewsItem{
    String url;
    int views;

    public MediaNewsItem(String title, Date date, Category category, String url, int views){
        super(title, date, category);
        this.url = url;
        this.views = views;
    }

    @Override
    public String getTeaser() {
        return String.format("%s\n%s\n%s\n%d\n", title,
                Math.abs(date.getMinutes()-LocalDateTime.now().getMinute()) + 60*Math.abs(date.getHours()-LocalDateTime.now().getHour()),
                url, views);
    }
}

class FrontPage{
    List<NewsItem> news;
    List<Category> allCategories;

    public FrontPage(Category [] categories){
        news = new LinkedList<>();
        allCategories = new LinkedList<>(Arrays.asList(categories.clone()));
    }

    public void addNewsItem(NewsItem newsItem) {
        news.add(newsItem);
    }

   public List<NewsItem> listByCategory(Category category){
        List<NewsItem> result;
        result = news.stream().filter(nNews -> nNews.getCategory().equals(category)).collect(Collectors.toList());
        return result;
    }

    public List<NewsItem> listByCategoryName(String category) throws CategoryNotFoundException {
        Category category1 = new Category(category);
        if(!allCategories.contains(category1))
            throw new CategoryNotFoundException(category1.getName());
        List<NewsItem> result;
        result = news.stream().filter(nNews -> nNews.getCategory().equals(category1)).collect(Collectors.toList());
        return result;
    }

    @Override
    public String toString(){
        StringBuilder result = new StringBuilder();
        for(NewsItem ni : news ){
            result.append(ni.getTeaser());
        }
        return result.toString();
    }
}

class CategoryNotFoundException extends Exception{
    public CategoryNotFoundException(String category){
        super(String.format("Category %s was not found", category));
    }
}

public class FrontPageTest {
    public static void main(String[] args) {
        // Reading
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        String[] parts = line.split(" ");
        Category[] categories = new Category[parts.length];
        for (int i = 0; i < categories.length; ++i) {
            categories[i] = new Category(parts[i]);
        }
        int n = scanner.nextInt();
        scanner.nextLine();
        FrontPage frontPage = new FrontPage(categories);
        Calendar cal = Calendar.getInstance();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            cal = Calendar.getInstance();
            int min = scanner.nextInt();
            cal.add(Calendar.MINUTE, -min);
            Date date = cal.getTime();
            scanner.nextLine();
            String text = scanner.nextLine();
            int categoryIndex = scanner.nextInt();
            scanner.nextLine();
            TextNewsItem tni = new TextNewsItem(title, date, categories[categoryIndex], text);
            frontPage.addNewsItem(tni);
        }

        n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            int min = scanner.nextInt();
            cal = Calendar.getInstance();
            cal.add(Calendar.MINUTE, -min);
            scanner.nextLine();
            Date date = cal.getTime();
            String url = scanner.nextLine();
            int views = scanner.nextInt();
            scanner.nextLine();
            int categoryIndex = scanner.nextInt();
            scanner.nextLine();
            MediaNewsItem mni = new MediaNewsItem(title, date, categories[categoryIndex], url, views);
            frontPage.addNewsItem(mni);
        }
        // Execution
        String category = scanner.nextLine();
        System.out.println(frontPage);
        for(Category c : categories) {
            System.out.println(frontPage.listByCategory(c).size());
        }
        try {
            System.out.println(frontPage.listByCategoryName(category).size());
        } catch(CategoryNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}