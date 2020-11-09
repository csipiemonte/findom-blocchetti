<#--
# Copyright Regione Piemonte - 2020
# SPDX-License-Identifier: EUPL-1.2-or-later
-->
 		<#-- script che disattiva la pressione del tasto "Enter" -->
		<script type="text/javascript">
			$('html').bind('keypress', function(e)
			{
			   if(e.keyCode == 13)
			   {
			      return false;
			   }
			});
		</script>
