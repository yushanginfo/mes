[#ftl]
[@b.head/]
[@b.toolbar title="修改产品工艺信息"]bar.addBack();[/@]
  [@b.form action=b.rest.save(productTechnic) theme="list"]
    [@b.select onchange="fetchScheme(this)" id="productSelect" label="产品信息" name="scheme.product.id" value=(productTechnic.scheme.product)! style="width:400px"
            required="true" option="id,title" href="/base/products.json?q={term}" empty="..." /]
    [@b.select label="工艺路线" id="schemeSelect" value=productTechnic.scheme! name="productTechnic.scheme.id"  style="width:400px" required="true" option="id,title" empty="..." /]
    [@b.textfield name="productTechnic.indexno" label="加工顺序" value=productTechnic.indexno! required="true" maxlength="80"/]
    [@b.select  label="工艺列表" name="productTechnic.technic.id" option="id,title" href="/base/technics.json?q={term}" empty="..."
        value=productTechnic.technic! option="id,title" required="true" style="width:400px"/]
    [@b.radios name="productTechnic.internal" label="性质" value=productTechnic.internal?string("1","0") items="1:厂内,0:委外" comment="厂内需要选择加工中心"/]
    [@b.select name="productTechnic.machine.id" label="加工中心" items=machines value=productTechnic.machine! option=r"${item.code} ${item.name}" required="false"/]
    [@b.textfield name="productTechnic.description" label="工艺说明" value=productTechnic.description! required="false" maxlength="100"/]
    [@b.formfoot]
      [@b.reset/]&nbsp;&nbsp;[@b.submit value="action.submit"/]
    [/@]
  [/@]
  [#list 1..10 as i]<br>[/#list]
    <script>
      function fetchScheme(select) {
        var thisObj = $(select);
        var productId = thisObj.val().trim();
        if (productId.length == 0) {
          $('#exchangeStudentSelect').empty();
        } else {
          $.ajax({
            "type": "POST",
            "url": "${b.url("/base/technic-schemes.json?scheme.product.id=")}"+productId,
            "async": false,
            "dataType": "json",
            "success": function(data) {
              $('#schemeSelect').empty();
              if(data.length>0){
               for(var i=0;i< data.length;i++){
                 $('#schemeSelect').append($('<option>', {
                      value: data[i].id,
                      text : data[i].title
                 }));
               }
               $("#schemeSelect option[value='${(productTechnic.scheme.id)!}']").attr("selected","selected");
              }else{
                $('#schemeSelect').empty();
              }
            }
          });
        }
      }
      [#if (productTechnic.scheme.id)??]
      $(document).ready(function() {
        fetchScheme(document.getElementById("productSelect"));
      });
      [/#if]
    </script>
[@b.foot/]
