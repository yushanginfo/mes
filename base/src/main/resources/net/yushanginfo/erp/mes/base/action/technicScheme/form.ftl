[#ftl]
[@b.head/]
[@b.toolbar title="修改工艺路线"]bar.addBack();[/@]
  [@b.form name="technicSchemeForm" action=b.rest.save(technicScheme) theme="list"]
    [@b.select label="产品信息" name="technicScheme.product.id" value=technicScheme.product! style="width:400px"
        required="true" option="id,title" href="/base/products.json?q={term}" empty="..." /]
    [@b.textfield name="technicScheme.indexno" label="编号" value="${technicScheme.indexno!}" required="true" maxlength="80"/]
    [@b.textfield name="technicScheme.name" label="名称" value="${technicScheme.name!}" required="true" maxlength="80"/]
    [@b.select multiple="multiple" label="工艺列表" name="technicIds" option="id,title" href="/base/technics.json?q={term}"
        values=technicScheme.technics option="id,title" required="true" style="width:400px"/]
    [@b.formfoot]
      [@b.reset/]&nbsp;&nbsp;[@b.submit value="action.submit"/]
    [/@]
  [/@]
[@b.foot/]
