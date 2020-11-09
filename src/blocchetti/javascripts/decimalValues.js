<#--
# Copyright Regione Piemonte - 2020
# SPDX-License-Identifier: EUPL-1.2-or-later
-->
<#-- script per la gestione dei numeri decimali -->
		<script type="text/javascript">
   function formattaDecimalePerCalc(val){
	   if(val.trim() == ""){ return "0";}
       retVal = val;
       if(val.indexOf(",")!=-1){
       	retVal = retVal.replace(",",".");
       }
       return retVal;
   }

   function formattaDecimalePerVis(val){
	  val = String(val);
	  if(val.indexOf(',')!=-1){
		  val = val.replace(',','.');//se il numero avesse la virgola il round di sotto darebbe NaN
	  }
   	  val = (Math.round(val*100)/100).toFixed(2);
      retVal = String(val);
      if(retVal.indexOf('.')!=-1){
         retVal = retVal.replace('.',',');
      }
      if(retVal=="0,00"){
    	 retVal = "";
      }
      return retVal;
   }
   
   function verificaFormatoNumerico(val){
	  if(val.trim() == ''){ 
		 return true;
	  }	  
      if(val.substring(0,1)==='-'){
   	     return false;
      }      
      // ATTENZIONE startsWith non è supportato da Internet Explorer
      /*if(val.startsWith(",")) {
          return false;
	  }*/
      if(val.indexOf(",")==0) {
    	  return false;
      }
      if(val.indexOf(',')!=-1){
    	  val = val.replace(',','.');
    	  decimali = val.substring(val.indexOf('.')+1);
    	  if(decimali.length>2){
             return false;
         }
      }     
      if(!$.isNumeric(val)){
   	     return false;
      }      
      if(val.indexOf('E')!=-1){
   	     return false;
      }
      if(val.indexOf('e')!=-1){
   	     return false;
      }      
      return true;
   }
   
   /*
    * verifica se valore rappresenta correttamente un numero decimale con al piu' due decimali;
    * il parametro ammessoNeg stabilisce se i valori negativi sono da considerarsi validi;
    * il parametro ammessoVuoto stabilisce se il valore stringa vuota deve considerarsi un numero decimale valido oppure no
    * il parametro ammessoZero stabilisce se il valore 0  deve considerarsi un numero decimale valido oppure no
    */
   function validaDecimaleJs(valore, ammessoNeg, ammessoVuoto, ammessoZero){ // 30.90, false, true, false
	   //<![CDATA[
		if(typeof valore == 'undefined') {
	      return false;
	    }
		
		if(valore.trim() == '' && valore.length>0) {  //non sono ammessi spazi
			 return false;
		}
		if(valore.trim() == '') {
			if(ammessoVuoto==true){
			   return true;
			}else {
			   return false;
			}
		}
		if(ammessoZero==false){ // 30.90 entra qui
			var tmp = valore.replace(',','.');
			if(parseFloat(tmp)===0){ // salta
				return false;
			}
		}  
		
	    var pattern = "";
	    if(ammessoNeg==true){ // non entra qui
	    	pattern = "^-?(([1-9]\\d*)|0)(,\\d{1,2})?$";
	    }else{
	    	pattern = "^(([1-9]\\d*)|0)(,\\d{1,2})?$"; // qui si: 
	    }

	    var regExpObj = new RegExp(pattern);
	    if(regExpObj.test(valore)){ // non supera il test
	        return true;
	    }
	   
		return false; // restituisce false con 30.10
	//]]>
	}
   
   //assume che il valore in input sia un numero decimale valido
   function formattaDecimalePerVisualizzazione(val){
	  var valStr = String(val);
	  if(valStr.indexOf(',')!=-1){
		  valStr = valStr.replace(',','.');//se il numero avesse la virgola il round di sotto darebbe NaN
	  }
	  var valNum = (Math.round(valStr*100)/100).toFixed(2);
	  var retVal = String(valNum);
	  if(retVal.indexOf('.')!=-1){
	     retVal = retVal.replace('.',',');
	  }
	  
	  return retVal;
	}
 </script>
