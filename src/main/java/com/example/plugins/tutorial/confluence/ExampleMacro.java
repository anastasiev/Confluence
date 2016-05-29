package com.example.plugins.tutorial.confluence;

import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.confluence.content.render.xhtml.XhtmlException;
import com.atlassian.confluence.macro.Macro;
import com.atlassian.confluence.macro.MacroExecutionException;
import com.atlassian.confluence.pages.Page;
import com.atlassian.confluence.pages.PageManager;
import com.atlassian.confluence.xhtml.api.MacroDefinition;
import com.atlassian.confluence.xhtml.api.MacroDefinitionHandler;
import com.atlassian.confluence.xhtml.api.XhtmlContent;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class ExampleMacro implements Macro
{

    private PageManager pageManager;

    public void setPageManager(PageManager pageManager) {
        this.pageManager = pageManager;
    }


    @Override
    public String execute(Map<String, String> parameters, String bodyContent, ConversionContext conversionContext) throws MacroExecutionException
    {

        long pageId = conversionContext.getEntity().getId();
        Page currentPage = pageManager.getPage(pageId);
        List<Page> pages = pageManager.getPages(currentPage.getSpace(), true);
        pages.sort(new Comparator<Page>() {
            public int compare(Page p1, Page p2) {
                int version1 = p1.getVersion();
                int version2 = p2.getVersion();

                if(version1 == version2)
                    return 0;
                else if(version1 < version2)
                    return 1;
                else
                    return -1;
            }
        });


        StringBuilder builder = new StringBuilder();
        builder.append("<p>");
        if (!pages.isEmpty())
        {
            builder.append("<table width=\"50%\">");
            builder.append("<tr><th>Title</th><th>Version</th></tr>");
            for (Page page : pages)
            {
                builder.append("<tr>");
                builder.append("<td>").append(page.getTitle()).append("</td><td>").append(String.valueOf(page.getVersion())).append("</td>");
                builder.append("</tr>");
            }
            builder.append("</table>");
        }
        else
        {
            builder.append("You haven't had any pages yet");
        }
        builder.append("</p>");

        return builder.toString();
        
    }
    
    @Override
    public BodyType getBodyType()
    {
        return BodyType.NONE;
    }

    @Override
    public OutputType getOutputType()
    {
        return OutputType.BLOCK;
    }
}
