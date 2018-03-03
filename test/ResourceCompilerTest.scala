package com.lucidchart.android.resources.test

import android.support.constraint.ConstraintLayout
import android.view._
import android.widget._
import com.lucidchart.android.resources._
import com.lucidchart.android.test.{TR, R}
import org.specs2.mutable.SpecificationWithJUnit

class ResourceCompilerTest extends SpecificationWithJUnit {

  "Resource compiler" should {
    "Generate a ID resources for a simple layout" in {
      TR.textView mustEqual TypedResource[TextView](R.id.textView)
    }

    "Generate ID resources for a complex layout" in {
      TR.wrap mustEqual TypedResource[ImageButton](R.id.wrap)
      TR.relativeWrap mustEqual TypedResource[ConstraintLayout](R.id.relativeWrap)
      TR.border mustEqual TypedResource[RelativeLayout](R.id.border)
      TR.editText mustEqual TypedResource[EditText](R.id.editText)
      TR.doneButton mustEqual TypedResource[ImageButton](R.id.doneButton)
      TR.bar mustEqual TypedResource[ConstraintLayout](R.id.bar)
      TR.button1 mustEqual TypedResource[ImageButton](R.id.button1)
      TR.button2 mustEqual TypedResource[ImageButton](R.id.button2)
      TR.button3 mustEqual TypedResource[ImageButton](R.id.button3)
      TR.textView mustEqual TypedResource[TextView](R.id.textView)
      TR.otherButton mustEqual TypedResource[ImageButton](R.id.otherButton)
      TR.exit mustEqual TypedResource[ImageButton](R.id.exit)
      TR.linearLayout mustEqual TypedResource[LinearLayout](R.id.linearLayout)
    }

    "Generate Layout resource for each layout" in {
      TR.layout.simple_view mustEqual TypedLayout[View](R.layout.simple_view)
      TR.layout.complex_view mustEqual TypedLayout[TextView](R.layout.complex_view)
    }
  }

}
