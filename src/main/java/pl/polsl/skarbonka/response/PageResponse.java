package pl.polsl.skarbonka.response;

import java.util.List;

public class PageResponse<T> {
    private final List<T> content;
    private final Integer page;
    private final Integer size;

    public PageResponse(List<T> content, Integer page, Integer size) {
        this.content = content;
        this.page = page;
        this.size = size;
    }

    public List<T> getContent() {
        return content;
    }

    public Integer getPage() {
        return page;
    }

    public Integer getSize() {
        return size;
    }
}
