/*
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-22, xufeng, Create file
 */
package com.tplink.cartoon.data.bean;

import com.tplink.cartoon.utils.StringConverter;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.util.List;

@Entity
public class DBDownloadItem extends BaseBean {

    @Id
    protected Long id;
    protected Long comic_id;
    //漫画标题
    protected String title;
    //章节标题
    protected String chapters_title;
    //章节编号
    protected int chapters;
    //章节漫画页面
    protected int num;
    //当前下载的漫画页面
    protected int current_num;
    //开始下载时间
    protected Long create_time;
    //最后更新时间
    protected Long update_time;
    /*state状态数据库保存*/
    protected int stateInte;

    @Convert(columnType = String.class, converter = StringConverter.class)
    protected List<String> chapters_path;

    @Convert(columnType = String.class, converter = StringConverter.class)
    protected List<String> chapters_url;

    public Long getUpdate_time() {
        return this.update_time;
    }

    public void setUpdate_time(Long update_time) {
        this.update_time = update_time;
    }

    public Long getCreate_time() {
        return this.create_time;
    }

    public void setCreate_time(Long create_time) {
        this.create_time = create_time;
    }

    public int getCurrentNum() {
        return this.current_num;
    }

    public void setCurrentNum(int current_num) {
        this.current_num = current_num;
    }

    public int getNum() {
        return this.num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getChapters() {
        return this.chapters;
    }

    public void setChapters(int chapters) {
        this.chapters = chapters;
    }

    public String getChapters_title() {
        return this.chapters_title;
    }

    public void setChapters_title(String chapters_title) {
        this.chapters_title = chapters_title;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getComic_id() {
        return this.comic_id;
    }

    public void setComic_id(Long comic_id) {
        this.comic_id = comic_id;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Generated(hash = 966925522)
    public DBDownloadItem(Long id, Long comic_id, String title, String chapters_title, int chapters,
                          int num, int current_num, Long create_time, Long update_time, int stateInte,
                          List<String> chapters_path, List<String> chapters_url) {
        this.id = id;
        this.comic_id = comic_id;
        this.title = title;
        this.chapters_title = chapters_title;
        this.chapters = chapters;
        this.num = num;
        this.current_num = current_num;
        this.create_time = create_time;
        this.update_time = update_time;
        this.stateInte = stateInte;
        this.chapters_path = chapters_path;
        this.chapters_url = chapters_url;
    }

    @Generated(hash = 255750031)
    public DBDownloadItem() {
    }

    @DownState.state
    public int getState() {
        switch (getStateInte()) {
            case 0:
                return DownState.START;
            case 1:
                return DownState.DOWN;
            case 2:
                return DownState.PAUSE;
            case 3:
                return DownState.STOP;
            case 4:
                return DownState.ERROR;
            case 5:
                return DownState.FINISH;
            case 6:
                return DownState.NONE;
            case -1:
                return DownState.DELETE;
            default:
                return DownState.FINISH;
        }
    }

    public void setState(int state) {
        setStateInte(state);
    }

    public List<String> getChaptersUrl() {
        return this.chapters_url;
    }

    public void setChapters_url(List<String> chapters_url) {
        this.chapters_url = chapters_url;
    }

    public int getStateInte() {
        return this.stateInte;
    }

    public void setStateInte(int stateInte) {
        this.stateInte = stateInte;
    }

    public List<String> getChaptersPath() {
        return this.chapters_path;
    }

    public void setChaptersPath(List<String> chapters_path) {
        this.chapters_path = chapters_path;
    }

    public int getCurrent_num() {
        return this.current_num;
    }

    public void setCurrent_num(int current_num) {
        this.current_num = current_num;
    }

    public List<String> getChapters_path() {
        return this.chapters_path;
    }

    public void setChapters_path(List<String> chapters_path) {
        this.chapters_path = chapters_path;
    }

    public List<String> getChapters_url() {
        return this.chapters_url;
    }
}
