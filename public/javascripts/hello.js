$(function() {
    // //  ajaxボタンが押された時
    // $('body').on('click', 'button[data-btn-type=ajax]', function(e) {
    //     console.log('click btn');
    //     //  リクエストの下準備
    //     //  リクエスト時に一緒に送るデータの作成
    //     var send_data;
    //     send_data = {
    //         //  テキストボックスの値を設定
    //         user_type : $('input').val()
    //     };
    //     console.log(send_data);
    //     //  WebAPIを叩く
    //     $.ajax({
    //         //  リクエストの内容
    //         type:"post",
    //         url: 'http://localhost:9000/upload',
    //         dataType: "json",
    //         data: JSON.stringify(send_data),
    //         contentType:'application/json',
    //         //  レスポンス成功時の処理
    //         success: function(responce) {
    //             if (responce.result === "OK") {
    //                 console.log(responce);
    //                 $('div[data-result=""]').html(JSON.stringify(responce));
    //             } else {
    //                 console.log(responce);
    //                 $('div[data-result=""]').html(JSON.stringify(responce));
    //             }
    //             return false;
    //         },
    //         //  レスポンス失敗時の処理
    //         error: function(XMLHttpRequest, textStatus, errorThrown) {
    //             console.log(XMLHttpRequest);
    //             console.log(textStatus);
    //             console.log(errorThrown);
    //             $('div[data-result=""]').html(JSON.stringify("データ取得中にエラーが発生しました。"));
    //             return false;
    //         }
    //     });
    //     //  フォーカスをテキストボックスに合わせる
    //     $('input').focus();
    //
    //     return false;
    // });
    //  searchボタンが押された時
    $('body').on('click', 'button[data-btn-type=search]', function(e) {
        console.log('click btn search');
        //  リクエストの下準備

        // var form = $('#myForm').get()[0];
        //  リクエスト時に一緒に送るデータの作成
        // var formData = new FormData($(this)[0]);
        var formData = new FormData();
        var $file = $('#file');
        var $text = $('#text');
        console.log($file.attr('name'))
        formData.append($file.attr('name'), $file.prop("files")[0]);
        formData.append($text.attr('name'), $text.val());
        console.log($file);
        console.log($text);
        console.log(formData.get("file"));
        // formData.append('file',$('#upload').get()[0]);
        // formData.append('file', $('input[type=file]')[0].files[0]);

        //  WebAPIを叩く
        // jQuery(function($) {
        //     $('#searchResultTable').DataTable({
        //         "ajax": {
        //             type: "POST",
        //             url: "upload",
        //             data: formData,
        //             enctype: 'json',
        //             processData: false,
        //             contentType: false
        //         },
        //         "columns": [
        //             {data: '検索ファイル名'},
        //             {data: '検索結果ファイル名'},
        //             {data: 'groupID'},
        //             {data: 'artifactID'},
        //             {data: 'version'},
        //             {data: 'jarファイル名'},
        //             {data: '類似度'}
        //         ]
        //     });
        // });
        $.ajax({
            //  リクエストの内容
            type: "POST",
            url: "upload",
            data: formData,
            enctype: 'json',
            processData: false,
            contentType: false,
        }).done(function( res ){
            console.log( 'SUCCESS', res);
            console.log( res.toString());
            console.log(JSON.parse(res.toString()));
            var tableHeaderRowCount = 1;
            var table = document.getElementById('searchResultTable');
            var rowCount = table.rows.length;
            for (var i = tableHeaderRowCount; i < rowCount; i++) {
                table.deleteRow(tableHeaderRowCount);
            }
            var items = [];
            $.each(JSON.parse(res.toString()), function (key, val) {
                items.push("<tr>");
                items.push("<td id=''"+key+"''>"+val.postedClassFile+"</td>");
                items.push("<td id=''"+key+"''>"+val.resultClassFile+"</td>");
                items.push("<td id=''"+key+"''>"+val.jar+"</td>");
                items.push("<td id=''"+key+"''>"+val.groupId+"</td>");
                items.push("<td id=''"+key+"''>"+val.artifactId+"</td>");
                items.push("<td id=''"+key+"''>"+val.version+"</td>");
                items.push("<td id=''"+key+"''>"+val.sim+"</td>");
                items.push("</tr>")
            });
            $("<tbody/>", {html: items.join("")}).appendTo("table");

            // $('#searchResultTable').DataTable({
            //     data: JSON.parse(res.toString()),
            //     columns: [
            //         {data: '検索ファイル名'},
            //         {data: '検索結果ファイル名'},
            //         {data: 'groupID'},
            //         {data: 'artifactID'},
            //         {data: 'version'},
            //         {data: 'jarファイル名'},
            //         {data: '類似度'}
            //     ]
            // });

        }).fail(function(jqXHR, textStatus, errorThrown){
            console.log( 'ERROR', jqXHR, textStatus, errorThrown)
        });
        return false;
    });
});
