package views.html.helper

package object ttBootstrap {

  implicit val ttField = new FieldConstructor {
    def apply(elts: FieldElements) = ttFieldConstructor(elts)
  }

}
