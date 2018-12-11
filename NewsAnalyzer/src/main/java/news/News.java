/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package news;

/**
 * Import section.
 */
import bloomfilter.BloomFilter;
import minhash.Document;
import minhash.Shingles;

/**
 * Métodos Probabilísticos Para a Engenharia Informática.
 * 2018-2019.
 * Prática 4.
 * Professor: António Teixeira.
 * Alunos: Rui Coelho e Vitor Fajardo.
 */

public class News extends Document {
    /**
     * Current ID initialized at 0.
     */
    private static int currentID = 0;
    /**
     * News ID.
     */
    private final int id;
    /**
     * News title.
     */
    private String title;
    /**
     * News content.
     */
    private String content;
    /**
     * BloomFilter instanciation.
     */
    private final BloomFilter filter;
    
    /**
     * Constuctor.
     * Builds BloomFolter with the words in the title and news content.
     * In new searchs generates the Shingle Set of the news..
     * 
     * @param title
     *          News title.
     * 
     * @param content 
     *          News content.
     */
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public News(String title, String content) {
        super(String.valueOf(currentID));
        this.setShingle(Shingles.shingling(title + " " + content, 5));
        id = currentID++;
        String words = (title + " " + content);
        String[] list = words.split("[ .,;-]");
        filter = new BloomFilter(list.length, 0.01);
        for (String word :list) {
            filter.insert(word.toLowerCase().getBytes());
        }
        this.title = title;
        this.content = content;
    }
    
    /**
     * Check if the words chosen by the user are in the news.
     * 
     * @param strings
     *          Words introduced by the user.
     * 
     * @return The amount of words found.
     *          In new searchs the results can be ordered by best matches.
     */
    public int search(String[] strings) {
        int out = 0;
        for (String x :strings)
            out += filter.isMember(x.toLowerCase().getBytes()) ? 1 : 0;
        return out;
    }
    
    /**
     * Title getter.
     * 
     * @return title -
     *          The news title.
     * 
     */
    public String getTitle() {
        return title;
    }
    
    /**
     * Title setter.
     * 
     * @param title 
     *          The news title.
     */
    public void setTitle(String title) {
        this.title = title;
    }
    
    /**
     * Content getter.
     * 
     * @return content
     *          - The news content.
     */
    public String getContent() {
        return content;
    }
    
    /**
     * Content setter.
     * 
     * @param content 
     *          The news content.
     */
    public void setContent(String content) {
        this.content = content;
    }
    
    /**
     * ID getter.
     * 
     * @return id
     *      - The news id.
     */
    public int getId() {
        return id;
    }
    
}
