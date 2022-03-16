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
                <Journal>
                    <PublisherName><xsl:value-of select="//asset/metadataList/metadata[@ofType='article']/copyright/statement/valueList/value[@valueType='normalized']/plainText" /></PublisherName>
                    <JournalTitle><xsl:value-of select="//asset/metadataList/metadata[@ofType='journal']/titleList/title/valueList/value[@valueType='normalized']/plainText" /></JournalTitle>
                    <Issn><xsl:value-of select="//asset/metadataList/metadata[@ofType='journal']/externalIdentifierList/externalIdentifier[@ofType='p-issn']/valueList/value/plainText" /></Issn>
                    <Volume><xsl:value-of select="//asset/metadataList/metadata[@ofType='issue']/taxonomyIdentifierList/taxonomyIdentifier[@ofType='volume']/valueList/value/plainText" /></Volume>
                    <Issue><xsl:value-of select="//asset/metadataList/metadata[@ofType='issue']/taxonomyIdentifierList/taxonomyIdentifier[@ofType='issue-number']/valueList/value/plainText" /></Issue>
                    <PubDate>
                        <Year><xsl:value-of select="//asset/metadataList/metadata[@ofType='article']/publicationHistoryList/publicationHistory/publicationDate/year/valueList/value/plainText" /></Year>
                        <Month><xsl:value-of select="//asset/metadataList/metadata[@ofType='article']/publicationHistoryList/publicationHistory/publicationDate/month/valueList/value/plainText" /></Month>
                        <Day><xsl:value-of select="//asset/metadataList/metadata[@ofType='article']/publicationHistoryList/publicationHistory/publicationDate/day/valueList/value/plainText" /></Day>
                    </PubDate>
                </Journal>
                <ArticleTitle><xsl:value-of select="//asset/metadataList/metadata[@ofType='article']/titleList/title/valueList/value/plainText" /></ArticleTitle>
                <FirstPage><xsl:value-of select="//asset/metadataList/metadata[@ofType='article']/pagination/pageRangeList/pageRange/firstPage/valueList/value/plainText" /></FirstPage>
                <LastPage><xsl:value-of select="//asset/metadataList/metadata[@ofType='article']/pagination/pageRangeList/pageRange/lastPage/valueList/value/plainText" /></LastPage>
                <Language><xsl:value-of select="substring(//asset/resourceList/resource[@ofType='languages']/languageList/language/definition/expanded/text(),1,3)" /></Language>
                <ArticleIdList>
                    <ArticleId IdType="doi"><xsl:value-of select="//asset/metadataList/metadata[@ofType='article']/externalIdentifierList/externalIdentifier[@ofType='doi']/valueList/value/plainText" /></ArticleId>
                    <ArticleId IdType="pii"><xsl:value-of select="//asset/metadataList/metadata[@ofType='article']/externalIdentifierList/externalIdentifier[@ofType='accession-number']/valueList/value/plainText" /></ArticleId>
                </ArticleIdList>
                <CopyrightInformation><xsl:value-of select="//asset/metadataList/metadata[@ofType='article']/copyright/statement/valueList/value[@valueType='normalized']/plainText" /></CopyrightInformation>
            </Article>
        </ArticleSet>
    </xsl:template>
</xsl:stylesheet>