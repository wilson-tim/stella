<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
   version="1.0">
  <xsl:output method="xml" encoding="UTF-8"/>
  <xsl:preserve-space elements="context property"/>

  <xsl:variable name="newline">
    <xsl:text>
    </xsl:text>
  </xsl:variable>
  
  <xsl:param name="cfgenv">dev</xsl:param>
  <xsl:param name="timestamp" select="''"/>
  
  <xsl:template match="/">
  <xsl:comment>
  =======================================================
  THIS FILE IS GENERATED - DO NOT EDIT
  TO ADD/OR CHANGE VALUES EDIT THE MASTER .ENV FILE
  DO NOT COMMIT THIS FILE TO CVS

  environment: <xsl:value-of select="$cfgenv"/>
  generated: <xsl:value-of select="$timestamp"/>
  =======================================================
  </xsl:comment>
  
  <xsl:value-of select="$newline"/>

  <configuration-context>
  
  <xsl:value-of select="$newline"/>
  <xsl:call-template name="contexts"/>
 
  </configuration-context>   
  </xsl:template>

  <xsl:template name="contexts">
    <xsl:for-each select="/configuration-context/context">
    
      <xsl:call-template name="print-context">
        <xsl:with-param name="context">
          <xsl:value-of select="."/>
        </xsl:with-param>
      </xsl:call-template>
    
      <xsl:value-of select="$newline"/>
  
    </xsl:for-each>
  </xsl:template>

 <xsl:template name="print-context">
  <xsl:param name="context"/>

  <xsl:if test="$context/@env = $cfgenv or not($context/@env)">
    <context>
       <xsl:attribute name="name">
        <xsl:value-of select="$context/@name"/>
      </xsl:attribute>
    
      <xsl:if test="$context/@read-only">
        <xsl:attribute name="read-only">
          <xsl:value-of select="$context/@read-only"/>
        </xsl:attribute>
      </xsl:if>

      <xsl:if test="$context/@inherit">
        <xsl:attribute name="inherit">
          <xsl:value-of select="$context/@inherit"/>
        </xsl:attribute>
      </xsl:if>

      <xsl:value-of select="$newline"/>
      
      <xsl:for-each select="$context/property">
  
        <xsl:call-template name="property">
          <xsl:with-param name="property">
            <xsl:value-of select="."/>
          </xsl:with-param>
          <xsl:with-param name="env">
            <xsl:value-of select="$context/@env"/>
          </xsl:with-param>
        </xsl:call-template>
 
      </xsl:for-each>
      
      <xsl:value-of select="$newline"/>      
      
    </context>
 
    <xsl:value-of select="$newline"/>
  </xsl:if>  
 </xsl:template>
 
 <xsl:template name="property">
  <xsl:param name="property"/>
  <xsl:param name="env"/>

   <xsl:choose>
    <xsl:when test="not($env)">
      <xsl:if test="$property/@env = $cfgenv or not($property/@env)">
        <xsl:call-template name="print-property">
          <xsl:with-param name="property">
            <xsl:value-of select="$property"/>
          </xsl:with-param>
        </xsl:call-template>
      </xsl:if>
    </xsl:when>
    <xsl:otherwise>
      <xsl:call-template name="print-property">
          <xsl:with-param name="property">
            <xsl:value-of select="$property"/>
          </xsl:with-param>
        </xsl:call-template>
    </xsl:otherwise>
   </xsl:choose>
 </xsl:template>
 
 <xsl:template name="print-property">
  <xsl:param name="property"/>
  <xsl:value-of select="$newline"/>
   <property>
   
      <xsl:attribute name="name">
        <xsl:value-of select="$property/@name"/>
      </xsl:attribute>
          
      <xsl:if test="$property/@system">
        <xsl:attribute name="system">
          <xsl:value-of select="$property/@system"/>
        </xsl:attribute>
      </xsl:if>
          
    <xsl:value-of select="$property"/>
  
  </property>
  
 </xsl:template>
 
</xsl:stylesheet>