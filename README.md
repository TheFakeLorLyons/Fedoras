# NEW CARTHAGE UNIVERSITY

## Repository of Approved Materials

New Carthage University is pleased to provide a complete record of approved materials for instructional and administrative use.

The University maintains these materials in accordance with institutional schedule. Materials are reviewed, retained, and made available as authorized.

This repository contains instructional materials, institutional records, faculty materials, student exercises, computational demonstrations, and associated documents relating to the University's programs of study.

**Some materials have been withheld. Some materials have been withdrawn. These distinctions are administrative and should not be interpreted as substantive.**

---

## Access

The materials in this repository are maintained as a Clojure/Quarto project.

Users responsible for maintaining or reproducing the record should have the following installed:

```bash
# required
brew install clojure/tools/clojure        # or your platform's equivalent
brew install --cask quarto                # or https://quarto.org/docs/get-started/

# optional but recommended
brew install borkdude/brew/babashka
quarto install tinytex                    # required only for PDF output
```

Equivalent installation methods may be used where appropriate.

The University does not prescribe the use of Homebrew.

---

## The Ordinary Course of Use

Authorized users may perform the following operations:

```bash
bb watch          # view and update materials in a browser
bb outline        # review scene status and outstanding work
bb check          # verify all instructional materials
bb book           # produce the complete HTML edition
bb reading        # produce the reading edition
bb pdf            # produce the PDF edition
bb new-scene act3 5 "The Registrar"
```

Where Babashka is unavailable, the corresponding Clojure tasks may be run directly:

```bash
clojure -M:clay
clojure -M:book
clojure -M:pdf
```

Users experiencing difficulty reproducing the record should consult `TROUBLESHOOTING.md`.

**If the difficulty cannot be reproduced, no corrective action is required.**

---

## Organization of Materials

The principal configuration is `clay.edn`.

Its `:source-path` establishes the order in which instructional chapters appear. Each entry corresponds to one chapter of the record.

Materials may be removed from ordinary circulation by commenting out the relevant entry.

This arrangement is considered sufficient to prevent accidental discrepancies.

---

## Repository Structure

```text
clay.edn                  the spine
deps.edn                  dependencies and aliases
bb.edn                    authorized tasks

src/fedoras/
  screenplay.clj          instructional screenplay apparatus
  artifacts.clj           retained document forms
  handbook.clj            University regulations and epigraphs
  personae.clj            persons, as data
  victor.clj              Victor's code
  live.clj                browser-accessible exercises
  build.clj               authorized build operations
  engine/                 computational materials associated with the Acts

notebooks/
  index.clj               cover and enrolment
  front/                  personae, setting, curriculum, code
  courses/                approved syllabi
  act1/ … act5/           scene materials
  appendix/               Handbook, game, supplementary materials
  tex/                    PDF preparation materials

resources/fedoras/        stylesheet
tools/                    maintenance utilities
notes/                    workshop record
```

The workshop record is retained in full.

**Nothing is discarded.**

---

## Instructions for Contributors

Materials should be written according to the conventions established below.

### The Subject

The principal subject is not assigned a permanent name.

Within instructional materials, use:

```clojure
{pc}
{pc-full}
{pc-last}
```

and use `:pc` as the dialogue cue.

A name may be supplied by the reader through the enrolment field.

The selected name is retained locally by the browser and applied throughout the reading experience.

The default name supplied with the record is maintained in:

```text
src/fedoras/screenplay.clj
```

An authorized build may establish another name without altering the retained default.

A contributor should not enter a literal subject name into an individual scene.

**This requirement is not optional.**

---

## Drafting Materials

Drafting materials are retained separately from the reading edition.

`sp/beats` records outstanding work.

`sp/note` records marginal information, production instructions, and other material not intended for ordinary circulation.

These materials are visible in the working edition and suppressed from the reading and PDF editions.

The `bb outline` operation reports outstanding beats across the record.

This is the University's preferred measure of incomplete work.

A completed scene should not retain unresolved beats without explanation.

---

## Scene Materials

New scenes may be created with:

```bash
bb new-scene act3 5 "The Registrar"
```

A newly created scene contains the standard introductory materials:

```clojure
(sp/assets)
(sp/status :draft)
(hb/epigraph n)
```

The Handbook maintains thirty approved rules.

Unused rules may be identified through:

```clojure
(hb/unused)
```

Contributors are encouraged to consult the Handbook before introducing new procedures.

---

## Computational Materials

Some instructional materials contain executable code.

This code is retained as part of the record.

There are two principal forms.

### Notebook Code

Clojure contained in the scene materials is evaluated during construction of the book.

Results displayed beneath a code form are produced by the code during the build.

The computational materials associated with the Acts are therefore not illustrative examples. They are executable.

The project assembled by the protagonist is maintained in:

```text
src/fedoras/engine/
```

Each Act contributes material to the completed system.

### Live Materials

Certain exercises are made available through browser-based cells.

These cells use Scittle to evaluate ClojureScript locally in the reader's browser.

No material is transmitted to an external service.

Where the required runtime is unavailable, the material is presented as ordinary text.

---

## Editions

The repository produces several authorized forms of the record.

### Interactive Edition

```bash
bb book
```

Produces the complete HTML edition, including interactive materials, enrolment, live cells, and drafting apparatus.

### Reading Edition

```bash
bb reading
```

Produces the reading edition with drafting materials and other production apparatus suppressed.

This is the recommended edition for ordinary circulation.

### PDF Edition

```bash
bb pdf
```

Produces the print edition.

The PDF typography and associated preparation materials are maintained separately from the HTML edition.

---

## Reproduction

A contributor may reproduce an individual scene directly from the REPL:

```clojure
(require '[fedoras.build :as build])

(build/scene! "act1/s2_the_activities_fair.clj")
```

This is the preferred method when reviewing a single material.
(I typically just run -main in fedoras/build.clj)

A complete build is not necessary to establish whether an individual scene exists.

---

## Maintenance

The repository is maintained as a complete record.

Changes should be made through the established apparatus.

Do not modify generated materials directly.

Do not remove a material merely because it appears redundant.

Do not restore a material merely because it appears to be missing.

Consult the relevant schedule.

**A missing record is not necessarily an absent record.**

**An absent record is not necessarily a missing record.**

---

## Notice Concerning Historical Materials

The University's instructional program includes materials concerning the history of New Carthage and the period preceding its founding.

Historical materials have been reviewed according to applicable standards.

Some materials remain available for approved instruction.

Some materials are available only upon request.

Some materials are no longer held in the ordinary collection.

The Library's catalogue should be consulted before making a request.

Requests concerning materials that do not appear in the catalogue should be directed to the appropriate instructor.

**Please do not refer to closed collections by former names.**

**There is no instructional advantage in doing so.**

---

## Final Notice

New Carthage University is committed to the orderly preservation of knowledge.

The record is maintained.

The schedule is in force.

Materials are available as authorized.

Questions concerning the contents of this repository should be directed through the ordinary channels.

Questions concerning materials that cannot be located should be directed through the same channels.

Repeated requests will not accelerate review.

Thank you for your continued cooperation.

---

**NEW CARTHAGE UNIVERSITY**
*Learn what is required. Preserve what is approved. Serve where you are placed.*

[ARCHIVAL NOTICE: Previous version unavailable.]

[END OF APPROVED MATERIAL]
---