package com.fiap.inovagab.utils

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.Spinner
import android.widget.TextView

object Ui {
    const val PRIMARY = "#1E3A8A"
    const val SECONDARY = "#3B82F6"
    const val SUCCESS = "#10B981"
    const val WARNING = "#F59E0B"
    const val ERROR = "#EF4444"
    const val BACKGROUND = "#F3F4F6"
    const val TEXT_PRIMARY = "#1F2937"
    const val TEXT_SECONDARY = "#6B7280"
    const val WHITE = "#FFFFFF"

    data class NavItem(
        val label: String,
        val active: Boolean = false,
        val onClick: () -> Unit
    )

    fun dp(context: Context, value: Int): Int {
        return (value * context.resources.displayMetrics.density).toInt()
    }

    fun root(context: Context): LinearLayout {
        return LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.parseColor(BACKGROUND))
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
        }
    }

    fun scrollContent(context: Context): LinearLayout {
        return LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(context, 16), dp(context, 16), dp(context, 16), dp(context, 24))
        }
    }

    fun wrapInScroll(context: Context, content: LinearLayout): ScrollView {
        return ScrollView(context).apply {
            isFillViewport = false
            addView(content)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                1f
            )
        }
    }

    fun header(context: Context, title: String, subtitle: String? = null): LinearLayout {
        return LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(context, 24), dp(context, 42), dp(context, 24), dp(context, 24))
            background = GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                intArrayOf(Color.parseColor(PRIMARY), Color.parseColor(SECONDARY))
            )

            addView(text(context, title, 26, WHITE, true))

            if (!subtitle.isNullOrBlank()) {
                addView(text(context, subtitle, 14, "#DBEAFE", false).apply {
                    setPadding(0, dp(context, 6), 0, 0)
                })
            }
        }
    }

    fun text(context: Context, value: String, size: Int, color: String = TEXT_PRIMARY, bold: Boolean = false): TextView {
        return TextView(context).apply {
            text = value
            textSize = size.toFloat()
            setTextColor(Color.parseColor(color))
            typeface = if (bold) Typeface.DEFAULT_BOLD else Typeface.DEFAULT
            includeFontPadding = true
        }
    }

    fun sectionTitle(context: Context, value: String): TextView {
        return text(context, value, 18, TEXT_PRIMARY, true).apply {
            setPadding(0, dp(context, 10), 0, dp(context, 10))
        }
    }

    fun card(context: Context): LinearLayout {
        return LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(context, 16), dp(context, 16), dp(context, 16), dp(context, 16))
            background = rounded(WHITE, dp(context, 14))
            elevation = dp(context, 2).toFloat()
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = dp(context, 14)
            }
        }
    }

    fun statCard(context: Context, value: String, label: String, icon: String): LinearLayout {
        return card(context).apply {
            val row = LinearLayout(context).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER_VERTICAL
            }

            row.addView(text(context, value, 23, PRIMARY, true).apply {
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            })

            row.addView(text(context, icon, 22, TEXT_SECONDARY, false))
            addView(row)

            addView(text(context, label.uppercase(), 10, TEXT_SECONDARY, true).apply {
                setPadding(0, dp(context, 6), 0, 0)
            })
        }
    }

    fun actionCard(
        context: Context,
        title: String,
        description: String,
        badge: String? = null,
        onClick: (() -> Unit)? = null
    ): LinearLayout {
        return card(context).apply {
            val row = LinearLayout(context).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER_VERTICAL
            }

            val texts = LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            }

            texts.addView(text(context, title, 17, TEXT_PRIMARY, true))
            texts.addView(text(context, description, 13, TEXT_SECONDARY, false).apply {
                setPadding(0, dp(context, 6), 0, 0)
            })

            row.addView(texts)

            if (!badge.isNullOrBlank()) {
                row.addView(chip(context, badge, true))
            } else {
                row.addView(text(context, "›", 28, TEXT_SECONDARY, true))
            }

            addView(row)

            if (onClick != null) {
                isClickable = true
                setOnClickListener { onClick() }
            }
        }
    }

    fun primaryButton(context: Context, label: String, onClick: () -> Unit): TextView {
        return buttonBase(context, label, WHITE, PRIMARY, onClick)
    }

    fun secondaryButton(context: Context, label: String, onClick: () -> Unit): TextView {
        return TextView(context).apply {
            text = label
            textSize = 15f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(Color.parseColor(PRIMARY))
            gravity = Gravity.CENTER
            background = strokeRounded("#FFFFFF", dp(context, 12), PRIMARY)
            setPadding(dp(context, 16), dp(context, 14), dp(context, 16), dp(context, 14))
            setOnClickListener { onClick() }
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = dp(context, 12)
            }
        }
    }

    fun dangerButton(context: Context, label: String, onClick: () -> Unit): TextView {
        return buttonBase(context, label, WHITE, ERROR, onClick)
    }

    private fun buttonBase(context: Context, label: String, textColor: String, bgColor: String, onClick: () -> Unit): TextView {
        return TextView(context).apply {
            text = label
            textSize = 15f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(Color.parseColor(textColor))
            gravity = Gravity.CENTER
            background = rounded(bgColor, dp(context, 12))
            setPadding(dp(context, 16), dp(context, 14), dp(context, 16), dp(context, 14))
            setOnClickListener { onClick() }
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = dp(context, 12)
            }
        }
    }

    fun chip(context: Context, label: String, active: Boolean): TextView {
        return TextView(context).apply {
            text = label
            textSize = 12f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(Color.parseColor(if (active) WHITE else TEXT_SECONDARY))
            gravity = Gravity.CENTER
            background = rounded(if (active) PRIMARY else "#E5E7EB", dp(context, 50))
            setPadding(dp(context, 12), dp(context, 6), dp(context, 12), dp(context, 6))
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                rightMargin = dp(context, 8)
                bottomMargin = dp(context, 8)
            }
        }
    }

    fun statusChip(context: Context, status: String): TextView {
        val color = when (status.lowercase()) {
            "aprovada", "aprovado", "concluido", "concluído", "ativa", "roi positivo" -> SUCCESS
            "rejeitada", "inativa" -> ERROR
            "execucao", "execução", "em execução" -> SECONDARY
            else -> WARNING
        }

        return TextView(context).apply {
            text = status.uppercase()
            textSize = 10f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
            background = rounded(color, dp(context, 50))
            setPadding(dp(context, 10), dp(context, 5), dp(context, 10), dp(context, 5))
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = dp(context, 8)
            }
        }
    }

    fun progress(context: Context, percent: Int): LinearLayout {
        return LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            background = rounded("#E5E7EB", dp(context, 50))

            val safe = percent.coerceIn(0, 100)

            addView(View(context).apply {
                background = rounded(SUCCESS, dp(context, 50))
                layoutParams = LinearLayout.LayoutParams(0, dp(context, 8), safe.toFloat())
            })

            addView(View(context).apply {
                layoutParams = LinearLayout.LayoutParams(0, dp(context, 8), (100 - safe).toFloat())
            })

            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(context, 8)
            ).apply {
                topMargin = dp(context, 10)
                bottomMargin = dp(context, 8)
            }
        }
    }

    fun bottomNav(context: Context, items: List<NavItem>): LinearLayout {
        return LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            setBackgroundColor(Color.WHITE)
            elevation = dp(context, 8).toFloat()
            setPadding(0, dp(context, 8), 0, dp(context, 8))

            items.forEach { item ->
                addView(text(context, item.label, 12, if (item.active) PRIMARY else TEXT_SECONDARY, item.active).apply {
                    gravity = Gravity.CENTER
                    isClickable = true
                    setPadding(0, dp(context, 8), 0, dp(context, 8))
                    setOnClickListener { item.onClick() }
                    layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                })
            }
        }
    }

    fun input(context: Context, hint: String, multiline: Boolean = false, number: Boolean = false, password: Boolean = false): EditText {
        return EditText(context).apply {
            this.hint = hint
            setTextColor(Color.parseColor(TEXT_PRIMARY))
            setHintTextColor(Color.parseColor(TEXT_SECONDARY))
            textSize = 15f
            setSingleLine(!multiline)
            background = rounded(WHITE, dp(context, 12))
            setPadding(dp(context, 14), dp(context, 12), dp(context, 14), dp(context, 12))
            inputType = when {
                password -> InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                multiline -> InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
                number -> InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
                else -> InputType.TYPE_CLASS_TEXT
            }
            if (multiline) {
                minLines = 4
                gravity = Gravity.TOP
            }
        }
    }

    fun inputBox(context: Context, label: String, editText: EditText): LinearLayout {
        return LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            addView(text(context, label, 13, TEXT_PRIMARY, true).apply {
                setPadding(0, 0, 0, dp(context, 6))
            })
            addView(editText, LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ))
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = dp(context, 12)
            }
        }
    }

    fun spinner(context: Context, items: List<String>): Spinner {
        return Spinner(context).apply {
            adapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, items)
            background = rounded(WHITE, dp(context, 12))
            setPadding(dp(context, 10), 0, dp(context, 10), 0)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(context, 56)
            ).apply {
                bottomMargin = dp(context, 12)
            }
        }
    }

    fun rounded(color: String, radius: Int): GradientDrawable {
        return GradientDrawable().apply {
            setColor(Color.parseColor(color))
            cornerRadius = radius.toFloat()
        }
    }

    private fun strokeRounded(fill: String, radius: Int, stroke: String): GradientDrawable {
        return GradientDrawable().apply {
            setColor(Color.parseColor(fill))
            setStroke(2, Color.parseColor(stroke))
            cornerRadius = radius.toFloat()
        }
    }
}
