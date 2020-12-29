//
// Created by BONNe
// Copyright - 2020
//


package world.bentobox.visit.panels;


import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import world.bentobox.bentobox.api.panels.PanelItem;
import world.bentobox.bentobox.api.panels.builders.PanelBuilder;
import world.bentobox.bentobox.api.panels.builders.PanelItemBuilder;


public class GuiUtils
{
// ---------------------------------------------------------------------
// Section: Border around GUIs
// ---------------------------------------------------------------------


    /**
     * This method creates border of black panes around given panel with 5 rows.
     *
     * @param panelBuilder PanelBuilder which must be filled with border blocks.
     */
    public static void fillBorder(PanelBuilder panelBuilder)
    {
        GuiUtils.fillBorder(panelBuilder, 5, Material.BLACK_STAINED_GLASS_PANE);
    }


    /**
     * This method sets black stained glass pane around Panel with given row count.
     *
     * @param panelBuilder object that builds Panel.
     * @param rowCount in Panel.
     */
    public static void fillBorder(PanelBuilder panelBuilder, int rowCount)
    {
        GuiUtils.fillBorder(panelBuilder, rowCount, Material.BLACK_STAINED_GLASS_PANE);
    }


    /**
     * This method sets blocks with given Material around Panel with 5 rows.
     *
     * @param panelBuilder object that builds Panel.
     * @param material that will be around Panel.
     */
    public static void fillBorder(PanelBuilder panelBuilder, Material material)
    {
        GuiUtils.fillBorder(panelBuilder, 5, material);
    }


    /**
     * This method sets blocks with given Material around Panel with given row count.
     *
     * @param panelBuilder object that builds Panel.
     * @param rowCount in Panel.
     * @param material that will be around Panel.
     */
    public static void fillBorder(PanelBuilder panelBuilder, int rowCount, Material material)
    {
        // Only for useful filling.
        if (rowCount < 3)
        {
            return;
        }

        for (int i = 0; i < 9 * rowCount; i++)
        {
            // First (i < 9) and last (i > 35) rows must be filled
            // First column (i % 9 == 0) and last column (i % 9 == 8) also must be filled.

            if (i < 9 || i > 9 * (rowCount - 1) || i % 9 == 0 || i % 9 == 8)
            {
                panelBuilder.item(i, BorderBlock.getPanelBorder(material));
            }
        }
    }


// ---------------------------------------------------------------------
// Section: String Manipulators
// ---------------------------------------------------------------------


    /**
     * Simple splitter
     *
     * @param string - string to be split
     * @return list of split strings
     */
    public static List<String> stringSplit(String string)
    {
        // Remove all ending lines from string.
        string = string.replaceAll("([\\r\\n])", "\\|");
        string = ChatColor.translateAlternateColorCodes('&', string);
        // Check length of lines
        List<String> result = new ArrayList<>();

        Arrays.stream(string.split("\\|")).
            map(line -> Arrays.asList(line.split(System.getProperty("line.separator")))).
            forEach(result::addAll);

        // Fix colors, as splitting my lost that information.

        for (int i = 0, resultSize = result.size(); i < resultSize; i++)
        {
            // Remove empty spaces from string.
            String trimmed = result.get(i).trim();

            if (i > 0)
            {
                String lastColor = ChatColor.getLastColors(result.get(i - 1));
                trimmed = lastColor + trimmed;
            }

            result.set(i, trimmed);
        }

        return result;
    }


    /**
     * Simple splitter for all strings in list.
     *
     * @param stringList - list of string to be split
     * @return list of split strings
     */
    public static List<String> stringSplit(List<String> stringList)
    {
        if (stringList.isEmpty())
        {
            return stringList;
        }

        List<String> newList = new ArrayList<>(stringList.size());
        stringList.stream().map(GuiUtils::stringSplit).forEach(newList::addAll);
        return newList;
    }


// ---------------------------------------------------------------------
// Section: Private classes
// ---------------------------------------------------------------------


    /**
     * This BorderBlock is simple PanelItem but without item meta data.
     */
    private static class BorderBlock extends PanelItem
    {
        private BorderBlock(ItemStack icon)
        {
            super(new PanelItemBuilder().
                icon(icon.clone()).
                name(" ").
                description(Collections.emptyList()).
                glow(false).
                clickHandler(null));
        }


        /**
         * This method retunrs BorderBlock with requested item stack.
         *
         * @param material of which broder must be created.
         * @return PanelItem that acts like border.
         */
        private static BorderBlock getPanelBorder(Material material)
        {
            ItemStack itemStack = new ItemStack(material);
            itemStack.getItemMeta().setDisplayName(" ");

            return new BorderBlock(itemStack);
        }
    }
}
