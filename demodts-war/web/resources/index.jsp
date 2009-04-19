<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>
<%@ taglib prefix="dtsource" uri="http://dt-source.sf.net" %>
<html>
<head>
	<title>Demo - dtSource with DisplayTag</title>
	<link rel="stylesheet" href="css/demodts.css" type="text/css"/>
	<script src="ajax/prototype.js" type="text/javascript"></script>
	<script src="ajax/scriptaculous.js" type="text/javascript"></script>
</head>
<body>
	<center>
	<b>Hibernate-powered table</b>
	<br>
	<dtsource:hibernate pagesize="2" id="hibernateTable" list="users1" sizelist="sizeusers1" defaultsortName="id" entity="User" alias="u">
		select ~alias~ from ~entity~ as ~alias~ order by ~sort~
	</dtsource:hibernate>	
	<display:table name="users1" export="true" sort="external" pagesize="2" id="hibernateTable" partialList="true" size="sizeusers1" requestURI="">
		<display:column  sortable="true" sortName="username" title="Login">
			<dtsource:inplaceEditor entity="User" propertyId="id" property="username" valueId="${hibernateTable.id}" value="${hibernateTable.username}"/>
		</display:column>
		<display:column property="password" sortable="false" sortName="password" title="Password" />
		<display:column property="created" sortable="true" sortName="created" title="Created Date" />
		<display:setProperty name="demodts-hibernate.pdf" value="true" />
	</display:table>
	<br>
	<hr>
	<br>
	<b>JDBC-powered table</b>
	<br>
	<dtsource:jdbc pagesize="2" id="jdbcTable" list="users2" sizelist="sizeusers2" defaultsortName="suUserId" table="simple_user" alias="u">
		select * from ~table~ as ~alias~ order by ~sort~
	</dtsource:jdbc>	
	<display:table name="users2" export="true" sort="external" pagesize="2" id="jdbcTable" partialList="true" size="sizeusers2" requestURI="">
		<display:column property="suUserId" sortable="true" sortName="suUserId" title="Id #" />
		<display:column sortable="true" sortName="suUsername" title="Login">
			<dtsource:inplaceEditor table="simple_user" propertyId="suUserId" property="suUsername" valueId="${jdbcTable.suUserId}" value="${jdbcTable.suUsername}"/>
		</display:column>
		<display:column property="suPassword" sortable="false" sortName="suPassword" title="Password" />
	</display:table>
	<br>
	<hr>
	<br>
	<b>Filter example for JDBC table</b>
	<br>
	<dtsource:filter id="jdbcFilterTable" field="suUsername" requestURI="">
		ALL|*	A|A*		B|B*		C|C*		D|D*		E|E*		S|S*		X|X*		W|W*
	</dtsource:filter>	
	<dtsource:jdbc pagesize="2" id="jdbcFilterTable" list="users3" sizelist="sizeusers3" defaultsortName="suUserId" table="simple_user" alias="u">
		select * from ~table~ as ~alias~ order by ~sort~
	</dtsource:jdbc>	
	<display:table name="users3" sort="external" pagesize="2" id="jdbcFilterTable" partialList="true" size="sizeusers3" requestURI="">
		<display:column property="suUserId" sortable="true" sortName="suUserId" title="Id #" />
		<display:column property="suUsername" sortable="true" sortName="suUsername" title="Login" />
		<display:column property="suPassword" sortable="true" sortName="suPassword" title="Password" />
	</display:table>
	<br>
	<hr>
	<br>
	<b>Filter example for Hibernate table</b>
	<br>
	<dtsource:filter id="hibernateFilterTable" field="username" requestURI="">
		ALL|*	1|1*		2|2*		3|3*		X|X*		W|W*		Z|Z*
	</dtsource:filter>	
	<dtsource:hibernate pagesize="2" id="hibernateFilterTable" list="users4" sizelist="sizeusers4" defaultsortName="id" entity="User" alias="u">
		select ~alias~ from ~entity~ as ~alias~ order by ~sort~
	</dtsource:hibernate>	
	<display:table name="users4" sort="external" pagesize="2" id="hibernateFilterTable" partialList="true" size="sizeusers4" requestURI="">
		<display:column property="id" sortable="true" sortName="id" title="Id #" />
		<display:column property="username" sortable="true" sortName="username" title="Login" />
		<display:column property="password" sortable="true" sortName="password" title="Password" />
	</display:table>
	<br>
	<hr>
	<br>
	<b>Search example for Hibernate table</b>
	<br>
	<dtsource:search id="hibernateSearchTable" requestURI="">
		id
	</dtsource:search>	
	<dtsource:hibernate pagesize="2" id="hibernateSearchTable" list="users5" sizelist="sizeusers5" defaultsortName="id" entity="User" alias="u">
		select ~alias~ from ~entity~ as ~alias~ order by ~sort~
	</dtsource:hibernate>	
	<display:table name="users5" sort="external" pagesize="2" id="hibernateSearchTable" partialList="true" size="sizeusers5" requestURI="">
		<display:column property="id" sortable="true" sortName="id" title="Id #" />
		<display:column property="username" sortable="true" sortName="username" title="Login" />
		<display:column property="password" sortable="true" sortName="password" title="Password" />
	</display:table>
	<br>
	<hr>
	<br>
	<b>Search example for JDBC table</b>
	<br>
	<dtsource:search id="jdbcSearchTable" requestURI="">
		suUserId	suUsername|User		suPassword|Pswd
	</dtsource:search>	
	<dtsource:jdbc pagesize="2" id="jdbcSearchTable" list="users6" sizelist="sizeusers6" defaultsortName="suUserId" table="simple_user" alias="u">
		select * from ~table~ as ~alias~ order by ~sort~
	</dtsource:jdbc>	
	<display:table name="users6" sort="external" pagesize="2" id="jdbcSearchTable" partialList="true" size="sizeusers6" requestURI="">
		<display:column property="suUserId" sortable="true" sortName="suUserId" title="Id #" />
		<display:column property="suUsername" sortable="true" sortName="suUsername" title="Login" />
		<display:column property="suPassword" sortable="true" sortName="suPassword" title="Password" />
	</display:table>
	<br>
	<hr>
	<br>
	<b>Search and Filter example for Hibernate table</b>
	<br>
	<dtsource:filter id="hibernateSFTable" field="username" requestURI="">
		ALL|*	U|U*	Z|Z*
	</dtsource:filter>	
	<dtsource:search id="hibernateSFTable" requestURI="" searchBanner="<td> {0} <br> {1} </td> <td> {2} <br> {3} </td> <td> <b>{4}</b> <br> {5} </td> <td valign=\"bottom\"> {6} </td>" classSubmit="myCssSubmit">
		id	username	password
	</dtsource:search>	
	<dtsource:hibernate pagesize="2" id="hibernateSFTable" list="users7" sizelist="sizeusers7" defaultsortName="id" entity="User" alias="u">
		select ~alias~ from ~entity~ as ~alias~ order by ~sort~
	</dtsource:hibernate>	
	<display:table name="users7" sort="external" pagesize="2" id="hibernateSFTable" partialList="true" size="sizeusers7" requestURI="">
		<display:column property="id" sortable="true" sortName="id" title="Id #" />
		<display:column property="username" sortable="true" sortName="username" title="Login" />
		<display:column property="password" sortable="true" sortName="password" title="Password" />
	</display:table>
	<br>
    </center>
		
	
</body>
</html>