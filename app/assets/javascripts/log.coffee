jQuery ->
  fieldRe = /(.+)\[\d+\](.+)/
  renumberFields = (tableDom) ->
    rows = jQuery(".repeat-row",tableDom)
    if( rows.size() > 1 )
      jQuery(".repeat-del-btn",tableDom).removeClass("disabled")
    else
      jQuery(".repeat-del-btn",tableDom).addClass("disabled")
      
    rows.each (idx) ->
      jQuery('input',this).each ->
        [match,pre,post] = fieldRe.exec( this.name )
        newName = "#{pre}[#{idx}]#{post}"
        this.name = newName

  initAutocomplete = (dom) ->
    dom.autocomplete
      minLength: 0
      source: (req,res) ->
        jsRoutes.controllers.Application.categoriesAcJs( req.term ).ajax
          success: res

  renumberFields( jQuery('.repeat-table') )

  jQuery(".repeat-add-btn").click ->
    table = jQuery(this).parents(".repeat-table")
    last = jQuery('.repeat-row:last',table)
    toInsert = jQuery(last).clone()
    jQuery("input",toInsert).each -> this.value = ""
    initAutocomplete( jQuery(".category",toInsert) )
    jQuery(last).after( toInsert )
    renumberFields( table )

  jQuery(".repeat-del-btn").click ->
    unless ( jQuery(this).hasClass( "disabled" ) )
      row = jQuery(this).parents(".repeat-row")
      table = jQuery(row).parents(".repeat-table")
      row.remove()
      renumberFields( table )

  jQuery(".repeat-table .category:first").focus()
            
  jQuery(".dt-picker").datepicker
    format: "yyyy-mm-dd"
    
  initAutocomplete( jQuery(".category") )
