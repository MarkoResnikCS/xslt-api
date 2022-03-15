<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">
    <xsl:output method="xml" indent="yes"/>
    <xsl:template match="/">
        <xsl:text disable-output-escaping="yes">
            &lt;!DOCTYPE ArticleSet
            PUBLIC "-//NLM//DTD PubMed 2.8//EN" "https://dtd.nlm.nih.gov/ncbi/pubmed/in/PubMed.dtd"&gt;
        </xsl:text>
        <ArticleSet>
            <Article>
                <!--                <Journal>-->
                <!--                    <PublisherName>American Academy of Neurology</PublisherName>-->
                <!--                    <JournalTitle>Neurology</JournalTitle>-->
                <!--                    <Issn>0028-3878</Issn>-->
                <!--                    <Volume>62</Volume>-->
                <!--                    <Issue>8</Issue>-->
                <!--                    <PubDate>-->
                <!--                        <Year>2004</Year>-->
                <!--                        <Month>April</Month>-->
                <!--                        <Day>27</Day>-->
                <!--                    </PubDate>-->
                <!--                </Journal>-->
                <!--                <ArticleTitle>April 27 Highlights</ArticleTitle>-->
                <!--                <FirstPage>1244</FirstPage>-->
                <!--                <LastPage>1245</LastPage>-->
                <!--                <Language>ENG</Language>-->
                <!--                <ArticleIdList>-->
                <!--                    <ArticleId IdType="doi">10.1212/WNL.62.8.1244</ArticleId>-->
                <!--                    <ArticleId IdType="pii">00006114-200404270-00004</ArticleId>-->
                <!--                </ArticleIdList>-->
                <CopyrightInformation><xsl:value-of select="//asset/metadataList/metadata[@ofType='article']/copyright/statement/valueList/value[@valueType='normalized']/plainText" /></CopyrightInformation>
            </Article>
        </ArticleSet>
    </xsl:template>
</xsl:stylesheet>