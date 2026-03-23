package com.rental.houserental.template;

public class MailTemplate {

    public static String buildApprovalTemplate(String ownerName, String propertyTitle) {
        return """
    <!DOCTYPE html>
    <html>
    <head>
        <meta charset="UTF-8">
        <style>
            body {
                font-family: 'Segoe UI', Arial, sans-serif;
                background-color: #eef2f7;
                margin: 0;
                padding: 0;
            }

            .container {
                max-width: 600px;
                margin: 40px auto;
                background: #ffffff;
                border-radius: 14px;
                overflow: hidden;
                box-shadow: 0 8px 25px rgba(0,0,0,0.08);
            }

            .header {
                background: linear-gradient(135deg, #22c55e, #16a34a);
                color: white;
                padding: 22px;
                text-align: center;
                font-size: 24px;
                font-weight: bold;
                letter-spacing: 0.5px;
            }

            .content {
                padding: 30px;
                color: #334155;
            }

            .title {
                font-size: 20px;
                font-weight: 600;
                margin-bottom: 10px;
            }

            .success-box {
                background: #f0fdf4;
                padding: 15px;
                border-radius: 10px;
                margin: 20px 0;
                border-left: 5px solid #22c55e;
            }

            .property-box {
                background: #f8fafc;
                padding: 15px;
                border-radius: 8px;
                margin-top: 10px;
                font-weight: 500;
            }

            .badge {
                display: inline-block;
                margin-top: 10px;
                padding: 6px 14px;
                background: #22c55e;
                color: white;
                border-radius: 50px;
                font-size: 12px;
                font-weight: bold;
            }

            .btn {
                display: inline-block;
                padding: 12px 20px;
                background: linear-gradient(135deg, #22c55e, #16a34a);
                color: white !important;
                text-decoration: none;
                border-radius: 8px;
                margin-top: 25px;
                font-weight: 600;
            }

            .footer {
                text-align: center;
                font-size: 12px;
                color: #94a3b8;
                padding: 18px;
                background: #f8fafc;
            }
        </style>
    </head>

    <body>

        <div class="container">

            <div class="header">
                 RentalHub
            </div>

            <div class="content">

                <div class="title">
                    Hello %s 
                </div>

                <div class="success-box">
                     <strong>Congratulations!</strong> Your property has been approved.
                </div>

                <p>Your listing is now live and available for tenants.</p>

                <div class="property-box">
                    <strong>Property:</strong> %s <br/>
                    <span class="badge">APPROVED</span>
                </div>

                <p>You can now start receiving inquiries and bookings.</p>


                <p style="margin-top:20px;">
                    Thank you for choosing <strong>RentalHub</strong>.
                </p>

            </div>

            <div class="footer">
                © 2026 RentalHub • Kanchanpur, Nepal <br/>
                Making renting simple & smart
            </div>

        </div>

    </body>
    </html>
    """.formatted(ownerName, propertyTitle);
    }
    public static String buildRejectedTemplate(String ownerName, String propertyTitle) {
        return """
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <style>
        body {
            font-family: 'Segoe UI', Arial, sans-serif;
            background-color: #eef2f7;
            margin: 0;
            padding: 0;
        }

        .container {
            max-width: 600px;
            margin: 40px auto;
            background: #ffffff;
            border-radius: 14px;
            overflow: hidden;
            box-shadow: 0 8px 25px rgba(0,0,0,0.08);
        }

        .header {
            background: linear-gradient(135deg, #ef4444, #dc2626);
            color: white;
            padding: 22px;
            text-align: center;
            font-size: 24px;
            font-weight: bold;
        }

        .content {
            padding: 30px;
            color: #334155;
        }

        .title {
            font-size: 20px;
            font-weight: 600;
            margin-bottom: 10px;
        }

        .error-box {
            background: #fef2f2;
            padding: 15px;
            border-radius: 10px;
            margin: 20px 0;
            border-left: 5px solid #ef4444;
        }

        .property-box {
            background: #f8fafc;
            padding: 15px;
            border-radius: 8px;
            margin-top: 10px;
            font-weight: 500;
        }

        .badge {
            display: inline-block;
            margin-top: 10px;
            padding: 6px 14px;
            background: #ef4444;
            color: white;
            border-radius: 50px;
            font-size: 12px;
            font-weight: bold;
        }

        .btn {
            display: inline-block;
            padding: 12px 20px;
            background: linear-gradient(135deg, #ef4444, #dc2626);
            color: white !important;
            text-decoration: none;
            border-radius: 8px;
            margin-top: 25px;
            font-weight: 600;
        }

        .footer {
            text-align: center;
            font-size: 12px;
            color: #94a3b8;
            padding: 18px;
            background: #f8fafc;
        }
    </style>
</head>

<body>

    <div class="container">

        <div class="header">
            RentalHub
        </div>

        <div class="content">

            <div class="title">
                Hello %s
            </div>

            <div class="error-box">
                <strong>We're sorry!</strong> Your property submission was not approved.
            </div>

            <p>After reviewing your listing, we found that it does not meet our current requirements.</p>

            <div class="property-box">
                <strong>Property:</strong> %s <br/>
                <span class="badge">REJECTED</span>
            </div>

            <p>You may review the details, make necessary changes, and submit again.</p>

            <p style="margin-top:20px;">
                If you have any questions, feel free to contact our support team.
            </p>

            <p>
                Thank you for choosing <strong>RentalHub</strong>.
            </p>

        </div>

        <div class="footer">
            © 2026 RentalHub • Kanchanpur, Nepal <br/>
            Making renting simple & smart
        </div>

    </div>

</body>
</html>
""".formatted(ownerName, propertyTitle);
    }
    public static String buildResetLink(String name, String resetLink) {
        return """
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <style>
        body {
            font-family: 'Segoe UI', Arial, sans-serif;
            background-color: #eef2f7;
            margin: 0;
            padding: 0;
        }

        .container {
            max-width: 600px;
            margin: 40px auto;
            background: #ffffff;
            border-radius: 14px;
            overflow: hidden;
            box-shadow: 0 8px 25px rgba(0,0,0,0.08);
        }

        .header {
            background: linear-gradient(135deg, #ef4444, #dc2626);
            color: white;
            padding: 22px;
            text-align: center;
            font-size: 24px;
            font-weight: bold;
        }

        .content {
            padding: 30px;
            color: #334155;
            text-align: center;
        }

        .btn {
            display: inline-block;
            padding: 12px 20px;
            background: linear-gradient(135deg, #ef4444, #dc2626);
            color: white !important;
            text-decoration: none;
            border-radius: 8px;
            margin-top: 20px;
            font-weight: 600;
        }

        .footer {
            text-align: center;
            font-size: 12px;
            color: #94a3b8;
            padding: 18px;
            background: #f8fafc;
        }
    </style>
</head>

<body>

    <div class="container">

        <div class="header">
            RentalHub Password Reset
        </div>

        <div class="content">

            <h2>Hello %s,</h2>

            <p>You requested to reset your password.</p>

            <p>Click the button below to reset your password:</p>

            <a href="%s" class="btn">Reset Password</a>

            <p style="margin-top:20px;">
                If you did not request this, please ignore this email.
            </p>

        </div>

        <div class="footer">
            © 2026 RentalHub
        </div>

    </div>

</body>
</html>
""".formatted(name, resetLink);
    }
}