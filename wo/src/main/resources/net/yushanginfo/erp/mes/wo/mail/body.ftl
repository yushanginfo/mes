<!DOCTYPE html>
<html lang="zh_CN">
  <head>
    <title></title>
    <meta http-equiv="content-type" content="text/html;charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <meta http-equiv="pragma" content="no-cache"/>
    <meta http-equiv="cache-control" content="no-cache"/>
  </head>
  <body>
  <div style="width: 100%;padding-right: 15px;padding-left: 15px;margin-right: auto;margin-left: auto;max-width: 1140px;box-sizing: border-box;">
<div style="position: relative;display: flex;flex-direction: column;min-width: 0;/*! word-wrap: break-word; */background-color: #fff;background-clip: border-box;border: 1px solid rgba(0,0,0,.125);border-radius: .25rem;box-sizing: border-box;">
  <div style="flex: 1 1 auto;min-height: 1px;padding: 1.25rem;">
    <h5 style="margin-bottom: .75rem;font-size: 1.25rem;font-weight: 500;line-height: 1.2;margin-top: 0;">工单评审提醒</h5>
    <p style="margin-top: 0;margin-bottom: 1rem;">${user.name}您好（工号:${user.code}），您所在${factory.name}的【${assessGroup.name}】审核组，有${workOrders?size}待评审工单需要评审,简要列表如下:</p>
    <a href="${ems.base}/mes/wo" style="display: inline-block;font-weight: 400;text-align: center;vertical-align: middle;user-select: none;border: 1px solid transparent;padding: .375rem .75rem;font-size: 1rem;line-height: 1.5;border-radius: .25rem;transition: color .15s ease-in-out,background-color .15s ease-in-out,border-color .15s ease-in-out,box-shadow .15s ease-in-out;text-decoration: none;color: #fff;background-color: #007bff;border-color: #007bff;">去评审</a>
        <table style="width: 100%;margin-bottom: 1rem;color: #212529;border-collapse: collapse;padding: .3rem;border-top: 1px solid #dee2e6;text-align: left;vertical-align: top;border-top: 1px solid #dee2e6;vertical-align: top;border-top: 1px solid #dee2e6;">
          [#assign tdStyle]padding: .3rem;vertical-align: top;border-top: 1px solid #dee2e6;[/#assign]
          <thead>
            <tr>
             <th style="${tdStyle}">工单单号</th>
             <th style="${tdStyle}">产品图号</th>
             <th style="${tdStyle}">产品名称</th>
             <th style="${tdStyle}">数量</th>
             <th style="${tdStyle}">客户交期</th>
             <th style="${tdStyle}">创建于</th>
             <th style="${tdStyle}">状态</th>
           </tr>
          </thead>
          <tbody>
          [#list workOrders as order]
           <tr style="vertical-align: top;border-top: 1px solid #dee2e6;">
            <td style="${tdStyle}">${order.batchNum}</td>
            <td style="${tdStyle}">${order.product.specification!}</td>
            <td style="${tdStyle}">${order.product.name}</td>
            <td style="${tdStyle}">${order.amount}</td>
            <td style="${tdStyle}">${(order.deadline?string("yyyy-MM-dd"))!}</td>
            <td style="${tdStyle}">${order.createdAt?string("yyyy-MM-dd HH:mm")}</td>
            <td style="${tdStyle}">${order.assessStatus.name}</td>
           </tr>
           [/#list]
          </tbody>
         </table>
    </div>
  </div>

 </div> <!--container-->
  </body>
</html>
