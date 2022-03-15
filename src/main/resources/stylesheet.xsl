<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
				xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" indent="yes" />
	<xsl:template match="/">
<!--		<xsl:text disable-output-escaping='yes'>-->
<!--&lt;!DOCTYPE ArticleSet-->
<!--  PUBLIC "-//NLM//DTD PubMed 2.8//EN" "https://dtd.nlm.nih.gov/ncbi/pubmed/in/PubMed.dtd"&gt;-->
<!--</xsl:text>-->
		<Element><xsl:value-of select="asset/auxiliaryType" /></Element>
	</xsl:template>
</xsl:stylesheet>