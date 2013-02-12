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

  removeRow = (dom) ->
    unless ( jQuery(dom).hasClass( "disabled" ) )
      row = jQuery(dom).parents(".repeat-row")
      table = jQuery(row).parents(".repeat-table")
      row.remove()
      renumberFields( table )


  decorateRepeatRow = (row) ->
    initAutocomplete( jQuery(".category",row) )
    jQuery(".repeat-del-btn" , row ).click -> removeRow(this)

  addRow = (button) ->
    table = jQuery(button).parents(".repeat-table")
    last = jQuery('.repeat-row:last',table)
    removeTabHelpTogglers()
    toInsert = jQuery(last).clone()
    jQuery("input",toInsert).each -> this.value = ""
    decorateRepeatRow( toInsert )
    jQuery(last).after( toInsert )
    renumberFields( table )
    addTabHelpTogglers()

  addTabHelpTogglers = () ->
    jQuery(".repeat-table .minutes:last").on( 
      "focus.repeatTable"
      () -> jQuery("#tab-help").removeClass( "hidden" )
    )

    jQuery(".repeat-table .minutes:last").on( 
      "blur.repeatTable"
      () -> jQuery("#tab-help").addClass( "hidden" )
    )

  removeTabHelpTogglers = () ->     
    jQuery(".repeat-table .minutes:last").off( ".repeatTable" )

  renumberFields( jQuery('.repeat-table') )
  addTabHelpTogglers()

  jQuery(".repeat-add-btn").click -> addRow( this )
  jQuery(".repeat-add-btn").focus -> 
    addRow( this )
    jQuery(".repeat-table .category:last").focus()

  jQuery(".repeat-row").each -> decorateRepeatRow( this )

  jQuery(".repeat-table .category:first").focus()
            
  jQuery(".dt-picker").datepicker
    format: "yyyy-mm-dd"
