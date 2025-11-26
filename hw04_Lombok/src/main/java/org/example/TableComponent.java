package org.example;

import enums.Colors;
import enums.Materials;
import enums.MaxLoadKG;
import enums.Options;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.Value;

@Getter
@ToString
@Builder
@Value
public class TableComponent {
    Colors colors;
    Materials materials;
    Options options;
    MaxLoadKG maxLoadKG;
}
