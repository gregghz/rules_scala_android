package com.lucidchart.android.resources

import cats.Applicative
import com.lucidchart.open.xtract._

object Instances {
  implicit val parseResultApplicative: Applicative[ParseResult] = new Applicative[ParseResult] {
    def pure[A](a: A): ParseResult[A] = ParseSuccess(a)
    def ap[A, B](ff: ParseResult[A => B])(fa: ParseResult[A]): ParseResult[B] = (ff, fa) match {
      case (ParseSuccess(f), ParseSuccess(a)) => ParseSuccess(f(a))
      case (ParseSuccess(f), PartialParseSuccess(a, errs)) => PartialParseSuccess(f(a), errs)
      case (PartialParseSuccess(f, errs), ParseSuccess(a)) => PartialParseSuccess(f(a), errs)
      case (PartialParseSuccess(f, errs1), PartialParseSuccess(a, errs2)) => PartialParseSuccess(f(a), errs1 ++ errs2)
      case (mf2, ParseFailure(errs)) => ParseFailure(mf2.errors ++ errs)
      case (ParseFailure(errs), ma2) => ParseFailure(errs ++ ma2.errors)
    }

    override def map[A, B](fa: ParseResult[A])(f: A => B): ParseResult[B] = fa.map(f)

    override def product[A, B](fa: ParseResult[A], fb: ParseResult[B]): ParseResult[(A,B)] = (fa, fb) match {
      case (ParseSuccess(a), ParseSuccess(b)) => ParseSuccess((a, b))
      case (ParseSuccess(a), PartialParseSuccess(b, errs)) => PartialParseSuccess((a, b), errs)
      case (PartialParseSuccess(a, errs), ParseSuccess(b)) => PartialParseSuccess((a, b), errs)
      case (PartialParseSuccess(a, errs1), PartialParseSuccess(b, errs2)) => PartialParseSuccess((a, b), errs1 ++ errs2)
      case (a, ParseFailure(errs)) => ParseFailure(a.errors ++ errs)
      case (ParseFailure(errs), b) => ParseFailure(errs ++ b.errors)
    }
  }
}
