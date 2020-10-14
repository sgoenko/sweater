<#import "parts/common.ftlh" as c>
<#include "parts/security.ftlh">

<@c.page>
<h5>Hello, <#if name=="unknown">guest<#else>${name}</#if></h5>
<div>It's a simple clone of Twitter</div>
</@c.page>
