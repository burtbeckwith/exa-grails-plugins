<table id="${id}" class="table display ${cssClass}" cellspacing="0" width="100%">
    ${raw(body)}
    <tbody></tbody>
</table>

<script type="text/javascript" charset="utf-8">
    $(document).ready(function() {
        new Exa.Datatable("${id}", ${data}, "${columns}")
            ._init();
    });
</script>