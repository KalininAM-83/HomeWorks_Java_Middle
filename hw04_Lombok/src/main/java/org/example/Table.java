package org.example;

import lombok.*;

@Getter
@ToString
@Builder(toBuilder = true)
@Value
public class Table {
    TableComponent tableTop;
    TableComponent motor;
    TableComponent underTable;
    TableComponent option;
}
