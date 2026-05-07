package com.canarypets.backend.DTOs;

import java.time.LocalDate;

public class OrderFilterFormDTO {

    private String status;
    private String search;
    private LocalDate dateFrom;
    private LocalDate dateTo;

    public String getStatus() {return status;}
    public void setStatus(String status) {this.status = status;}

    public String getSearch() {return search;}
    public void setSearch(String search) {this.search = search;}

    public LocalDate getDateFrom() {return dateFrom;}
    public void setDateFrom(LocalDate dateFrom) {this.dateFrom = dateFrom;}

    public LocalDate getDateTo() {return dateTo;}
    public void setDateTo(LocalDate dateTo) {this.dateTo = dateTo;}
}