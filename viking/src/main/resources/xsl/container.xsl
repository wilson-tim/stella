<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    version="1.0">
  <xsl:output method="xml" encoding="UTF-8" />
  <xsl:preserve-space 
    elements="container filters component parameters parameter properties property"/>

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

  <xsl:comment>
  ========================================================
    Components will be registered in the order listed below
  =========================================================
  </xsl:comment>
  
  <xsl:value-of select="$newline"/>
  
  <container>
    <xsl:value-of select="$newline"/>
  
    <xsl:call-template name="filters"/>
    <xsl:value-of select="$newline"/>

    <xsl:call-template name="components"/>
    <xsl:value-of select="$newline"/>
  </container>  
</xsl:template>

<!-- template for filters -->
  <xsl:template name="filters">
    <filters>
      <xsl:value-of select="$newline"/> 
      <xsl:for-each select="/container/filters/component">
        <xsl:call-template name="apply-compenv">
          <xsl:with-param name="component">
            <xsl:value-of select="."/>
          </xsl:with-param>
        </xsl:call-template>
     </xsl:for-each>
    </filters>
</xsl:template>

<!-- template for components -->
<xsl:template name="components">
  <components>
    <xsl:value-of select="$newline"/>
    <xsl:for-each select="/container/components/component">
        <xsl:call-template name="apply-compenv">
          <xsl:with-param name="component">
            <xsl:value-of select="."/>
          </xsl:with-param>
        </xsl:call-template>
    </xsl:for-each> 
    <xsl:value-of select="$newline"/>
  </components>
</xsl:template>

<xsl:template name="apply-compenv">
  <xsl:param name="component"/>
    
    <xsl:if test="$component/@env = $cfgenv or not($component/@env)">
      
    <component>
      <xsl:attribute name="class">
        <xsl:value-of select="$component/@class"/>
      </xsl:attribute>
      
      <xsl:if test="@name">
        <xsl:attribute name="name">
          <xsl:value-of select="$component/@name"/>
        </xsl:attribute>
      </xsl:if>
        
      <xsl:attribute name="class">
        <xsl:value-of select="$component/@class"/>
      </xsl:attribute>
          
      <xsl:if test="$component/@priority">
        <xsl:attribute name="priority">
          <xsl:value-of select="$component/@priority"/>
        </xsl:attribute>
      </xsl:if>
      
    <xsl:value-of select="$newline"/>
    <parameters>
      <xsl:for-each select="$component/parameters/parameter">
        <xsl:call-template name="comp-params">
          <xsl:with-param name="param">
            <xsl:value-of select="."/>
          </xsl:with-param>
          <xsl:with-param name="env">
            <xsl:value-of select="$component/@env"/>
          </xsl:with-param>
          
        </xsl:call-template>
      </xsl:for-each>
      <xsl:value-of select="$newline"/>
    </parameters>
    
    <xsl:value-of select="$newline"/>
    
    <properties>
      <xsl:for-each select="$component/properties/property">
          <xsl:call-template name="properties">
            <xsl:with-param name="property">
              <xsl:value-of select="."/>
            </xsl:with-param>
            <xsl:with-param name="env">
              <xsl:value-of select="$component/@env"/>
            </xsl:with-param>
          </xsl:call-template>
      </xsl:for-each>
      <xsl:value-of select="$newline"/>
    </properties>
    <xsl:value-of select="$newline"/>
    </component>
    <xsl:value-of select="$newline"/>
    </xsl:if>
</xsl:template>

<xsl:template name="comp-params">
  <xsl:param name="param"/>
  <xsl:param name="env"/>
  <xsl:choose>
    <xsl:when test="not($env)">
      <xsl:if test="$param/@env = $cfgenv or not($param/@env)">
        <xsl:call-template name="parameter">
          <xsl:with-param name="parameter">
            <xsl:value-of select="$param"/>
          </xsl:with-param>
        </xsl:call-template>
      </xsl:if>
    </xsl:when>

    <xsl:otherwise>

      <xsl:call-template name="parameter">
          <xsl:with-param name="parameter">
            <xsl:value-of select="$param"/>
          </xsl:with-param>
        </xsl:call-template>
    </xsl:otherwise>

  </xsl:choose>
</xsl:template>

<xsl:template name="parameter">
  <xsl:param name="parameter"/>

  <xsl:value-of select="$newline"/>
  <parameter>

    <xsl:attribute name="type">
      <xsl:value-of select="$parameter/@type"/>
    </xsl:attribute>
            
    <xsl:attribute name="value">
      <xsl:value-of select="$parameter/@value"/>
    </xsl:attribute>

    <xsl:if test="$parameter/@class">
      <xsl:attribute name="class">
        <xsl:value-of select="$parameter/@class"/>
      </xsl:attribute>
    </xsl:if>
      <xsl:value-of select="$parameter"/>
  </parameter>
</xsl:template>

<xsl:template name="properties">
  <xsl:param name="property"/>
  <xsl:param name="env"/>
  
  <xsl:choose>
    <xsl:when test="not($env)">
      <xsl:if test="$property/@env = $cfgenv or not($property/@env)">
        <xsl:call-template name="property">
          <xsl:with-param name="property">
            <xsl:value-of select="$property"/>
          </xsl:with-param>
        </xsl:call-template>
      </xsl:if>
    </xsl:when>

    <xsl:otherwise>

      <xsl:call-template name="property">
          <xsl:with-param name="property">
            <xsl:value-of select="$property"/>
          </xsl:with-param>
        </xsl:call-template>
    </xsl:otherwise>

  </xsl:choose>
  
</xsl:template>

<xsl:template name="property">
  <xsl:param name="property"/>
 
    <xsl:value-of select="$newline"/>
    <property>
      <xsl:attribute name="name">
          <xsl:value-of select="$property/@name"/>
        </xsl:attribute>
      
        <xsl:if test="$property/@type">
          <xsl:attribute name="type">
            <xsl:value-of select="$property/@type"/>
          </xsl:attribute>
        </xsl:if>
        <xsl:value-of select="$property"/>
    </property>
 
</xsl:template>
</xsl:stylesheet>