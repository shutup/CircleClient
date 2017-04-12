package com.shutup.circle.model.response;

import com.shutup.circle.model.persis.Question;

import java.util.List;

/**
 * Created by shutup on 2017/4/12.
 */
public class QuestionListResponse {
    /**
     * content : []
     * last : true
     * totalPages : 2
     * totalElements : 11
     * size : 10
     * number : 1
     * sort : [{"direction":"DESC","property":"createdAt","ignoreCase":false,"nullHandling":"NATIVE","ascending":false}]
     * first : false
     * numberOfElements : 1
     */

    private boolean last;
    private int totalPages;
    private int totalElements;
    private int size;
    private int number;
    private boolean first;
    private int numberOfElements;
    private List<Question> content;
    private List<SortEntity> sort;

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public List<Question> getContent() {
        return content;
    }

    public void setContent(List<Question> content) {
        this.content = content;
    }

    public List<SortEntity> getSort() {
        return sort;
    }

    public void setSort(List<SortEntity> sort) {
        this.sort = sort;
    }

    public static class SortEntity {
        /**
         * direction : DESC
         * property : createdAt
         * ignoreCase : false
         * nullHandling : NATIVE
         * ascending : false
         */

        private String direction;
        private String property;
        private boolean ignoreCase;
        private String nullHandling;
        private boolean ascending;

        public String getDirection() {
            return direction;
        }

        public void setDirection(String direction) {
            this.direction = direction;
        }

        public String getProperty() {
            return property;
        }

        public void setProperty(String property) {
            this.property = property;
        }

        public boolean isIgnoreCase() {
            return ignoreCase;
        }

        public void setIgnoreCase(boolean ignoreCase) {
            this.ignoreCase = ignoreCase;
        }

        public String getNullHandling() {
            return nullHandling;
        }

        public void setNullHandling(String nullHandling) {
            this.nullHandling = nullHandling;
        }

        public boolean isAscending() {
            return ascending;
        }

        public void setAscending(boolean ascending) {
            this.ascending = ascending;
        }
    }
}
