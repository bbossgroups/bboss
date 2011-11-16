<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet 
	version="2.0" 
	xmlns="http://www.w3.org/1999/xhtml" 
	xmlns:html="http://www.w3.org/1999/xhtml"
 	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:ant="http://www.fiset.ca/ant-docs/v1"
>

<xsl:output 
	method="xml" 
	encoding="UTF-8"
	standalone="yes"
	version="1.0"
	doctype-public="-//W3C//DTD XHTML 1.0 Strict//EN" 
	doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd" 
	indent="yes"/>

<!-- =================================================== -->
<xsl:template match="/ant:types">
	<html>
		<head>
			<META HTTP-EQUIV="CONTENT-TYPE" CONTENT="text/html; charset=windows-1252"/>
			<title>
				<xsl:value-of select="ant:title"/>
			</title>
			<META HTTP-EQUIV="Content-Language" CONTENT="en-us"/>
			<style type="text/css">	
				<![CDATA[
				body {
					font: 80% Verdana, Arial, Helvetica, sans-serif;
					margin: 5px; padding: 0;
				/*	background: rgb(95%,95%,80%);  */
					color: black;
				}
				h1 {
					font-size: 200%; letter-spacing: 3px;
					margin-bottom: 1em; 
					padding: 0.66em 0 0.33em 1em;
					background: rgb(85%,85%,70%);
				}
				h2 {
					background: rgb(90%,90%,80%);
				}
				h3 {
					background-color: rgb(95%,95%,85%);
				}
				.sample {
					font-family: Courier, "Courier New", monospace;
					background-color: #f3f1f4;
				 }
				div.sample {
					margin-left: 10px;
					margin-right: 10px;
					padding-top: 3px;
					padding-bottom: 3px;
					padding-left: 3px;
					padding-right: 3px;
					border:1px dashed black;
					width: 80%;
				 }
				 table.params {
					width: 80%;
					border: 1px solid gray;
					margin-bottom:1em;
				 }
				 th.params, td.params {
					padding: 0 0.5em;
					border-left: 1px solid #DDD;
					border-bottom: 1px solid #DDD;
					font: 70%
				}
				th.params {
					
				}
				table.toc {
					width: 80%;
					border: 1px solid gray;
					margin-bottom:1em;
				}
				td.toc {
					padding: 0 0.5em;
					border: none
					font: 70%
				}
				table.type {
					width: 80%;
					border: none;
					margin-bottom:1em;
				}
				th.type, td.type {
					text-align: left;
					padding: 0 0.5em;
					border: none
					font: 70%
				}
				th.type {
					font-weight: bold;
				}
				]]>
			</style>
		</head>
		<body lang="en-US" dir="LTR">
			<!-- Table of content -->
			<h1>
				<xsl:value-of select="ant:title"/>
			</h1>
			
			<h2>Table of Content</h2>
			<table class="toc">
				<tbody class="toc">
					<xsl:for-each select="ant:type">
						<tr class="toc">
							<td class="toc">
								<a>
									<xsl:attribute name="href">
										<xsl:text>#</xsl:text>
										<xsl:value-of select="@name"/>
									</xsl:attribute>
									
									<xsl:value-of select="@name"/>
								</a>
							</td>
							<td class="toc">
								<xsl:value-of select="ant:shortDescription"/>
							</td>
						</tr>
					</xsl:for-each>
				</tbody>
			</table>

			<h2>Introduction</h2>
			
			<xsl:apply-templates select="ant:introduction"/>
						

			<!-- Insert all defined types here -->
			<xsl:apply-templates select="ant:type"/>
			
		</body>
	</html>
</xsl:template>	

<!-- =================================================== -->
<xsl:template match="ant:type">
	<h1>
		<a>
			<xsl:attribute name="name" select="@name"/>
			<xsl:value-of select="@name"/>
		</a>
	</h1>
	
	<p>
		<xsl:value-of select="ant:shortDescription"/>
	</p>
	
	<xsl:if test="@className">
		<table class="type">
			<tbody class="type">
				<tr class="type">
					<th class="type">Implementation:</th>
					<td class="type">
						<xsl:value-of select="@className"/>
					</td>
				</tr>
			</tbody>
		</table>
	</xsl:if>

	<h2>Parameters</h2>
	<xsl:choose>
		<xsl:when test="count(./ant:parameter) = 0">
			<xsl:text>No parameters defined for this type.</xsl:text>
		</xsl:when>
		<xsl:otherwise>
			<table class="params">
				<tr class="params">
					<th class="params">Attribute</th>
					<th class="params">Description</th>
					<th class="params">Required</th>
				</tr>
				<xsl:apply-templates select="ant:parameter"/>
			</table>
		</xsl:otherwise>
	</xsl:choose>

	<h2>Nested Types</h2>
	<xsl:choose>
		<xsl:when test="count(./ant:nestedType) = 0">
			<xsl:text>No nested types defined for this type.</xsl:text>
		</xsl:when>
		<xsl:otherwise>
			<xsl:apply-templates select="ant:nestedType"/>
		</xsl:otherwise>
	</xsl:choose>

	<!-- Full description -->
	<h2>Description</h2>
	<xsl:apply-templates select="ant:fullDescription/child::node()"/>
				
</xsl:template>	

<!-- =================================================== -->
<xsl:template match="ant:parameter">
	<tr class="params">
		<td class="params">
			<xsl:value-of select="@name"/>
		</td>
		<td class="params">
			<xsl:apply-templates select="ant:description"/>
		</td>
		<td class="params">
			<xsl:apply-templates select="ant:required"/>
		</td>
	</tr>	
</xsl:template>

<!-- =================================================== -->
<xsl:template match="ant:nestedType">
	<h3>
		<xsl:value-of select="@name"/>
	</h3>
	
	<!-- Description -->
	<xsl:apply-templates select="ant:description/child::node()"/>
				
</xsl:template>	

<!-- =================================================== -->
<xsl:template match="ant:example">
	<div class="sample">
		<pre class="sample">
			<xsl:apply-templates select="child::node()"/>
		</pre>
	</div>
</xsl:template>

<!-- =================================================== -->
<xsl:template match="*|@*|text()|comment()">
	<xsl:copy>
		<xsl:apply-templates select="@*"/>
		<xsl:apply-templates select="child::node()"/>
	</xsl:copy>
</xsl:template>
		
</xsl:stylesheet>