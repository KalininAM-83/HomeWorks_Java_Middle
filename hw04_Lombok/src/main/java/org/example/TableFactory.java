package org.example;

import enums.Colors;
import enums.Materials;
import enums.MaxLoadKG;
import enums.Options;

public class TableFactory {
    public static void main(String[] args) {
        Table woodTable = Table.builder()
                .tableTop(TableComponent.builder()
                        .colors(Colors.DARK_BROWN)
                        .materials(Materials.Wood)
                        .build())
                .motor(TableComponent.builder()
                        .colors(Colors.BLACK)
                        .materials(Materials.Steel)
                        .maxLoadKG(MaxLoadKG._300)
                        .build())
                .underTable(TableComponent.builder()
                        .colors(Colors.BLACK)
                        .materials(Materials.Steel)
                        .maxLoadKG(MaxLoadKG._300)
                        .build())
                .option(TableComponent.builder()
                        .options(Options.USB)
                        .build())
                .option(TableComponent.builder()
                        .options(Options.WIRELESS_CHARGING)
                        .build())
                .build();

            Table plasticTable = woodTable.toBuilder()
                    .tableTop(TableComponent.builder()
                            .colors(Colors.WHITE)
                            .materials(Materials.Plastic)
                            .maxLoadKG(MaxLoadKG._100)
                            .build())
                    .option(TableComponent.builder()
                            .options(Options.CABLE_CHANNEL)
                            .build())
                    .build();

        System.out.println(woodTable);
        System.out.println(plasticTable);
    }
}
